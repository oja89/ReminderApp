package com.example.project

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.project.database.AppDatabase
import com.example.project.databinding.ActivityReminderAdderBinding
import java.text.SimpleDateFormat
import java.util.*


class ReminderAdder : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener
    {
    private lateinit var binding: ActivityReminderAdderBinding
    private lateinit var reminderCalendar: Calendar

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
            binding.txtUid.text = uidText

        // if we are editing, load the values from database with the uid

            // this has errors and problems
            AsyncTask.execute {
                val db = Room
                    .databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        getString(R.string.dbFilename)
                    )
                    .build()
                val dbData = db.reminderDao().getWithUid(uid)
                binding.txtMessage.setText(dbData.message)
                binding.txtDate.setText(dbData.reminder_time)
                binding.txtReminderSeen.setText(dbData.reminder_seen)
                binding.txtCreatorId.setText(dbData.creator_id)
                binding.txtCreated.setText(dbData.creation_time)
                binding.txtLocationX.setText(dbData.location_x)
                binding.txtLocationY.setText(dbData.location_y)




                db.close()
            }


            //binding.txtDate.setText()


        }
        // else no preloading, show uid as "new
        else binding.txtUid.text = "New"


        // date picker
        binding.txtDate.inputType = InputType.TYPE_NULL
        binding.txtDate.isClickable = true
        binding.txtDate.setOnClickListener {
            reminderCalendar = GregorianCalendar.getInstance()
            DatePickerDialog(
                this,
                this,
                reminderCalendar.get(Calendar.YEAR),
                reminderCalendar.get(Calendar.MONTH),
                reminderCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }





        // listener for add new button
        binding.btnNewReminder.setOnClickListener {
            // validate values
            if (binding.txtMessage.text.isEmpty()) {
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
                // if uid is null, its a new one
                // else should update the previous
                uid = if (uid == 0){
                    null
                    }
                    else uid,
                message = binding.txtMessage.text.toString(),
                location_x = binding.txtLocationX.text.toString(),
                location_y = binding.txtLocationY.text.toString(),
                reminder_time = binding.txtDate.text.toString(),
                creation_time = binding.txtCreated.text.toString(),
                creator_id = binding.txtCreatorId.text.toString(),
                reminder_seen = binding.txtReminderSeen.text.toString()
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

                if (uid != 0) {
                    // if the reminder is edited...
                    db.reminderDao().updateReminder(reminderInfo)
                }
                else {
                    // if it is a new reminder
                    db.reminderDao().insert(reminderInfo).toInt()
                }
                db.close()
            }

        finish()
        }
    }

        override fun onDateSet(
            dailogView: DatePicker?,
            selectedYear: Int,
            selectedMonth: Int,
            selectedDayOfMonth: Int
        ) {
            reminderCalendar.set(Calendar.YEAR, selectedYear)
            reminderCalendar.set(Calendar.MONTH, selectedMonth)
            reminderCalendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            binding.txtDate.setText(simpleDateFormat.format(reminderCalendar.time))

            // if you want to show time picker after the date
            // you dont need this,change dateFormat value to dd.MM.yyyy
            TimePickerDialog(
                this,
                this,
                reminderCalendar.get(Calendar.HOUR_OF_DAY),
                reminderCalendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        override fun onTimeSet(view: TimePicker?, selectedhourOfDay: Int, selectedMinute: Int) {
            reminderCalendar.set(Calendar.HOUR_OF_DAY, selectedhourOfDay)
            reminderCalendar.set(Calendar.MINUTE, selectedMinute)
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            binding.txtDate.setText(simpleDateFormat.format(reminderCalendar.time))
        }
    }



