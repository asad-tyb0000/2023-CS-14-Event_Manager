package com.example.event_management_application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var photoView: ImageView
    private lateinit var labelName: TextView
    private lateinit var labelPhone: TextView
    private lateinit var labelEmail: TextView
    private lateinit var labelEvent: TextView
    private lateinit var labelDate: TextView
    private lateinit var labelGender: TextView
    private lateinit var successBanner: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        bindConfirmViews()
        populateDetails()
    }

    private fun bindConfirmViews() {
        photoView = findViewById(R.id.ivConfirmPhoto)
        labelName = findViewById(R.id.tvConfirmName)
        labelPhone = findViewById(R.id.tvConfirmPhone)
        labelEmail = findViewById(R.id.tvConfirmEmail)
        labelEvent = findViewById(R.id.tvConfirmEventType)
        labelDate = findViewById(R.id.tvConfirmDate)
        labelGender = findViewById(R.id.tvConfirmGender)
        successBanner = findViewById(R.id.tvSuccessMsg)
    }

    private fun populateDetails() {
        val extras = intent

        labelName.text = extras.getStringExtra("EXTRA_NAME") ?: "N/A"
        labelPhone.text = extras.getStringExtra("EXTRA_PHONE") ?: "N/A"
        labelEmail.text = extras.getStringExtra("EXTRA_EMAIL") ?: "N/A"
        labelEvent.text = extras.getStringExtra("EXTRA_EVENT") ?: "N/A"
        labelDate.text = extras.getStringExtra("EXTRA_DATE") ?: "N/A"
        labelGender.text = extras.getStringExtra("EXTRA_GENDER") ?: "N/A"

        val photoUriStr = extras.getStringExtra("EXTRA_PHOTO_URI")
        if (!photoUriStr.isNullOrEmpty()) {
            photoView.setImageURI(Uri.parse(photoUriStr))
        }

        successBanner.text = "Registration Successful!"
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(homeIntent)
        finish()
    }
}
