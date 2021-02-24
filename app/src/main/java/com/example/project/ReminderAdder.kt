package com.example.project

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.project.database.AppDatabase
import com.example.project.databinding.ActivityReminderAdderBinding

class ReminderAdder : AppCompatActivity() {
    private lateinit var binding: ActivityReminderAdderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        // create binding
        binding = ActivityReminderAdderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // find if there is a uid passed (editing)
        // use intents getintextra methods
        var intent = getIntent()
        val uid = intent.getIntExtra("uid", 0)

        // use uid to be seen in the edit window first
        val uidText = uid.toString()
        if (uidText != "0")  {
            binding.idDisplayer.text = uidText
        }
        else binding.idDisplayer.text = "New"


        // if we are editing, load the values from database with the uid




        // listener for add new button
        binding.btnNewReminder.setOnClickListener {
            // validate values
            if (binding.txtName.text.isEmpty()) {
                Toast.makeText(
                        applicationContext,
                        "Name should not be empty",
                        Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // if validated, take the vals as reminderInfo

            val reminderInfo = ReminderInfo(
            //message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen
                null,
                message = binding.txtName.text.toString(),
                location_x = "asdf".toString(),
                location_y = "dfwefe".toString(),
                reminder_time = binding.txtDate.text.toString(),
                creation_time = "gaga".toString(),
                creator_id = "afdf".toString(),
                reminder_seen = "afaf".toString()
            )

            // convert date to dd.mm.yyyy
                //do...

            // save to database
            AsyncTask.execute{
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                getString(R.string.dbFilename)
                ).build()

                val uuid = db.reminderDao().insert(reminderInfo).toInt()
                db.close()
            }

        finish()
        }
    }
}