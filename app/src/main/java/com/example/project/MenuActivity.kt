package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // string for listview
        val myStringArray = arrayOf<String>("This", "is", "placeholder")

        // do the thing with adapter for listView
        // adapter for strings
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray)
        // call for setAdapter...
        val myListView: ListView = findViewById(R.id.mainList)
        myListView.adapter = adapter

        // button for profile
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(
                    Intent(applicationContext, ProfileActivity::class.java)
            )

        }

        // button for logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // set logged in -> false?
            startActivity(
                    Intent(applicationContext, MainActivity::class.java)
            )
        }

        // button for adding new items to list
        findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener {
            startActivity(
                Intent(applicationContext, ProfileActivity::class.java)
            )
        }


    }
}
