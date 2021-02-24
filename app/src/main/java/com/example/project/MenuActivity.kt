package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityMenuBinding
import kotlinx.android.synthetic.main.activity_menu.view.*

class MenuActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //binding
        // create binding
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root

        //setContentView(R.layout.activity_menu)
        setContentView(view)

        // string for listview
        val myStringArray = arrayOf<String>("This", "is", "placeholder")

        // do the thing with adapter for listView
        // adapter for strings
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray)
        // call for setAdapter...
        //val myListView: ListView = findViewById(R.id.mainList)
        val myListView: ListView = view.mainList
        myListView.adapter = adapter

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


    }
}
