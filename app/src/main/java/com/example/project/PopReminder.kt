package com.example.project

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.project.database.AppDatabase
import com.example.project.databinding.ActivityPopReminderBinding

class PopReminder : AppCompatActivity() {
    private lateinit var binding: ActivityPopReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // bindings
        binding = ActivityPopReminderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // find out what was the notification
        val caller = getIntent()
        val uid = caller.getIntExtra("uid", 0)
        Toast.makeText(applicationContext, "This was $uid", Toast.LENGTH_SHORT).show()

        //load the values from database with the uid
        AsyncTask.execute {
            val db = Room
                .databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    getString(R.string.dbFilename)
                )
                .build()
            var dbData = db.reminderDao().getWithUid(uid)
            binding.txtPopMsg.setText(dbData.message)
            binding.txtPopDate.setText(dbData.reminder_time)

            // update the reminder to been seen
            dbData.reminder_seen = 1
            db.reminderDao().updateReminder(dbData)
        }
    }
}