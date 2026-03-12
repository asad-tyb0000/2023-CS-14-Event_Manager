package com.example.event_management_application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRegisterButton()
    }

    private fun setupRegisterButton() {
        val btnRegister = findViewById<Button>(R.id.btnRegisterEvent)
        btnRegister.setOnClickListener {
            val registrationIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }
    }
}
