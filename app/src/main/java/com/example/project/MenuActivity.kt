package com.example.project

import ReminderHistoryAdapter
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.project.database.AppDatabase
import com.example.project.databinding.ActivityMenuBinding
import kotlinx.android.synthetic.main.activity_menu.view.*


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

        setContentView(view)

        listView = binding.mainList

        refreshListView()



        // button for profile
        //findViewById<Button>(R.id.btnProfile).setOnClickListener {
        view.btnProfile.setOnClickListener{
            startActivity(
                    Intent(applicationContext, ProfileActivity::class.java)
            )
        }

        // button for logout
        //findViewById<Button>(R.id.btnLogout).setOnClickListener {
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
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }

    private fun refreshListView() {
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
            val reminderInfos = db.reminderDao().getReminderInfos()
            db.close()
            return reminderInfos
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
}
