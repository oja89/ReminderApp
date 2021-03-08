 package com.example.project

import ReminderHistoryAdapter
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.project.database.AppDatabase
import com.example.project.databinding.ActivityMenuBinding
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_menu.view.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class MenuActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMenuBinding
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        // create binding
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root

        // set listView
        setContentView(view)
        listView = binding.mainList
        refreshListView()


        // button for profile
        view.btnProfile.setOnClickListener{
            startActivity(
                    Intent(applicationContext, ProfileActivity::class.java)
            )
        }

        // button for viewall
        view.btnViewToggle.setOnClickListener{
            // change the view
            if (btnViewToggle.text == "Due") {
            btnViewToggle.text = ("All")
            }
            else {
                btnViewToggle.text = ("Due")
            }
            // then refresh
            refreshListView()
        }


        // button for logout
        view.btnLogout.setOnClickListener{
            // set logged in -> false?
            applicationContext.getSharedPreferences(
                    getString(R.string.sharedPreferences),
                    Context.MODE_PRIVATE
            ).edit().putInt("LoginStatus", 0).apply()

            startActivity(
                    Intent(applicationContext, MainActivity::class.java)
            )

        }

        // button for adding new items to list
        //findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
        view.btnAdd.setOnClickListener {
            startActivity(
                Intent(applicationContext, ReminderAdder::class.java)
            )
        }

        // clicking item on list...
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
            //retrieve selected Item

            val selectedReminder = listView.adapter.getItem(position) as ReminderInfo
            val message =
                "Do you want to delete or edit ${selectedReminder.message} reminder, on ${selectedReminder.reminder_time}?"

            // Show AlertDialog to delete the reminder
            val builder = AlertDialog.Builder(this@MenuActivity)
            builder.setTitle("Delete reminder?")
                .setMessage(message)
                .setPositiveButton("Delete") { _, _ ->
                    // Update UI


                    //delete from database
                    AsyncTask.execute {
                        val db = Room
                            .databaseBuilder(
                                applicationContext,
                                AppDatabase::class.java,
                                getString(R.string.dbFilename)
                            )
                            .build()
                        db.reminderDao().delete(selectedReminder.uid!!)
                    }


                    //refresh payments list
                    refreshListView()
                }

                .setNeutralButton("Edit") { _, _ ->
                    // Go to same menu where adding is done, but preload values?
                    val editReminder = Intent(applicationContext, ReminderAdder::class.java)
                    editReminder.putExtra("uid", selectedReminder.uid)
                    startActivity(editReminder)
                    finish()
                }



                .setNegativeButton("Cancel") { dialog, _ ->
                    // Do nothing
                    dialog.dismiss()
                }
                .show()



        }
        view.btnTest.setOnClickListener {
            startActivity(Intent(applicationContext, MapActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }

    private fun refreshListView() {
        // refresh the view
        var refreshTask = LoadReminderInfoEntries()
        refreshTask.execute()
    }

    inner class LoadReminderInfoEntries : AsyncTask<String?, String?, List<ReminderInfo>>() {
        override fun doInBackground(vararg params: String?): List<ReminderInfo> {
            val db = Room
                .databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    getString(R.string.dbFilename)
                )
                .build()

            // apply value from toggle button
            if (btnViewToggle.text == "All") {
                val reminderInfos = db.reminderDao().getReminderInfos()
                db.close()
                return reminderInfos
            }
            else {
                // take only reminders that have not yet been seen (and have no location)
                val dueReminders = db.reminderDao().getDueReminders(1)
                //val dueReminders = db.reminderDao().getDueReminders()
                db.close()
                return dueReminders
            }
        }

        override fun onPostExecute(reminderInfos: List<ReminderInfo>?) {
            super.onPostExecute(reminderInfos)
            if (reminderInfos != null) {
                if (reminderInfos.isNotEmpty()) {
                    listView.adapter = ReminderHistoryAdapter(applicationContext, reminderInfos)
                } else {
                    listView.adapter = null
                    Toast.makeText(applicationContext, "No items now", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        // reminder manager'


        @RequiresApi(Build.VERSION_CODES.O)
        fun showNofitication(context: Context, message: String, uid: Int) {

            // get intent to open with press of notification
            //val resultIntent = Intent(context, MenuActivity::class.java)

            // try to add uid in the intent
            val resultIntent = Intent(context, PopReminder::class.java)
            resultIntent.putExtra("uid", uid)

            val resultPendingIntent: PendingIntent= TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }


            val CHANNEL_ID = "REMINDER_APP_NOTIFICATION_CHANNEL"

            // why is there a randomizer?
            // guess that the id needs to be different?
            var notificationId = Random.nextInt(10, 1000) + 5

            var notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_account_box_24px)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(CHANNEL_ID)
                // open intent with click of notification
                .setContentIntent(resultPendingIntent)
                // remove the notification with click
                .setAutoCancel(true)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Notification chancel needed since Android 8
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.app_name)
            }
            notificationManager.createNotificationChannel(channel)

            // show the notification
            notificationManager.notify(notificationId, notificationBuilder.build())

        }


        fun setReminderWithWorkManager(
            context: Context,
            uid: Int,
            timeInMillis: Long,
            message: String
        ) {

            val reminderParameters = Data.Builder()
                .putString("message", message)
                .putInt("uid", uid)
                .build()

            // get minutes from now until reminder
            var minutesFromNow = 0L
            if (timeInMillis > System.currentTimeMillis())
                minutesFromNow = timeInMillis - System.currentTimeMillis()

            val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInputData(reminderParameters)
                .setInitialDelay(minutesFromNow, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(reminderRequest)
        }
    }
}
