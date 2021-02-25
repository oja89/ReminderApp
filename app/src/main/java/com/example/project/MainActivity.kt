package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        //setContentView(R.layout.activity_main)
        setContentView(view)

        // login button functions
        //findViewById<Button>(R.id.btnLogin).setOnClickListener {
        binding.btnLogin.setOnClickListener {
            var user = R.id.txtUsername
            var pass = R.id.txtPassword

            /// could not make this into HW1 deadline
            applicationContext.getSharedPreferences(
                getString(R.string.sharedPreferences),
                Context.MODE_PRIVATE
            ).edit().putInt("LoginStatus", 1).apply()


            // finally start menu activity
            startActivity(
                Intent(applicationContext, MenuActivity::class.java)
            )
        }
        checkLoginStatus()
    }
    override fun onResume() {
        super.onResume()
        checkLoginStatus()
    }


    private fun checkLoginStatus() {
        val loginStatus = applicationContext.getSharedPreferences(
            getString(R.string.sharedPreferences),
            Context.MODE_PRIVATE
        ).getInt("LoginStatus", 0)
        if (loginStatus == 1) {
            startActivity(Intent(applicationContext, MenuActivity::class.java))
        }
    }
}