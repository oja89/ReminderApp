package com.example.project

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // string for listview
        val myStringArray = arrayOf<String>("ased","adsfa","asdfla")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray)
        val listView: ListView = findViewById(R.id.mainList)
        listView.adapter = adapter

    }
}