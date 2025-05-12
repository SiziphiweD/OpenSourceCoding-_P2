package com.example.opensourcepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)

        // Set up the Edge-to-Edge layout (if necessary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the login button by its ID
        val loginButton: Button = findViewById(R.id.btnLogin)
        val signupButton: Button = findViewById(R.id.btnSignup)

        // Set a click listener on the login button
        loginButton.setOnClickListener {
            // Create an Intent to open MainActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)


        }
        // Set a click listener on the signup button
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

}
