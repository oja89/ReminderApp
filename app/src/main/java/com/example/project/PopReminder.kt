package com.example.project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PopReminder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_reminder)

        val caller = getIntent()
        val uid = caller.getIntExtra("uid", 0)
        Toast.makeText(applicationContext, "This was $uid", Toast.LENGTH_SHORT).show()
    }
}