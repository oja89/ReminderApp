package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
            val user: String = binding.txtUsername.text.toString()
            val pass = binding.txtPassword
            // these should be compared, not included (yet)



            //just save the username for creatorId purposes
            applicationContext.getSharedPreferences(
                getString(R.string.sharedPreferences),
                Context.MODE_PRIVATE
            ).edit().putString("Username", user).apply()


            // change the state of loginstatus
            applicationContext.getSharedPreferences(
                getString(R.string.sharedPreferences),
                Context.MODE_PRIVATE
            ).edit().putInt("LoginStatus", 1).apply()

            // check what we saved
            val userSaved = applicationContext.getSharedPreferences(
                getString(R.string.sharedPreferences),
                Context.MODE_PRIVATE
            ).getString("Username", "empty")
            // greet user
            Toast.makeText(applicationContext, "Hello $userSaved !", Toast.LENGTH_SHORT).show()

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
        // check if logged in
        val loginStatus = applicationContext.getSharedPreferences(
            getString(R.string.sharedPreferences),
            Context.MODE_PRIVATE
        ).getInt("LoginStatus", 0)
        if (loginStatus == 1) {
            // if logged in, open menuactivity instead
            startActivity(Intent(applicationContext, MenuActivity::class.java))
        }
    }
}