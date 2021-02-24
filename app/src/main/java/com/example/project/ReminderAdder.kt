package com.example.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val reminderInfo = Reminderinfo(
            name = binding.txtName.text.toString(),
            date = binding.txtDate.text.toString()
        )
    }
}