package com.example.event_management_application

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegistrationActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var phoneField: EditText
    private lateinit var emailField: EditText
    private lateinit var eventSpinner: Spinner
    private lateinit var datePickButton: Button
    private lateinit var dateLabel: TextView
    private lateinit var genderGroup: RadioGroup
    private lateinit var photoPreview: ImageView
    private lateinit var photoPickButton: Button
    private lateinit var termsCheck: CheckBox
    private lateinit var submitButton: Button

    private var chosenPhotoUri: Uri? = null
    private var chosenDateText: String = ""
    private var dateWasSelected = false
    private var pickedYear = 0
    private var pickedMonth = 0
    private var pickedDay = 0

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        bindViews()
        configureEventSpinner()
        configureDatePicker()
        configureImagePicker()
        configureSubmitAction()
    }

    private fun bindViews() {
        nameField = findViewById(R.id.etFullName)
        phoneField = findViewById(R.id.etPhone)
        emailField = findViewById(R.id.etEmail)
        eventSpinner = findViewById(R.id.spinnerEventType)
        datePickButton = findViewById(R.id.btnPickDate)
        dateLabel = findViewById(R.id.tvDateDisplay)
        genderGroup = findViewById(R.id.rgGender)
        photoPreview = findViewById(R.id.ivPickedImage)
        photoPickButton = findViewById(R.id.btnPickImage)
        termsCheck = findViewById(R.id.cbAcceptTerms)
        submitButton = findViewById(R.id.btnSubmitForm)
    }

    private fun configureEventSpinner() {
        val categories = arrayOf(
            "Select Event Type", "Seminar", "Workshop",
            "Conference", "Webinar", "Cultural Event"
        )
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventSpinner.adapter = spinnerAdapter
    }

    private fun configureDatePicker() {
        datePickButton.setOnClickListener {
            val now = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    pickedYear = year
                    pickedMonth = month
                    pickedDay = day
                    chosenDateText = "$day/${month + 1}/$year"
                    dateLabel.text = chosenDateText
                    dateWasSelected = true
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun configureImagePicker() {
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { outcome ->
            if (outcome.resultCode == RESULT_OK && outcome.data != null) {
                chosenPhotoUri = outcome.data?.data
                photoPreview.setImageURI(chosenPhotoUri)
            }
        }

        photoPickButton.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            galleryLauncher.launch(pickIntent)
        }
    }

    private fun configureSubmitAction() {
        submitButton.setOnClickListener { runValidation() }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun runValidation() {
        val name = nameField.text.toString().trim()
        val phone = phoneField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val eventCategory = eventSpinner.selectedItem.toString()
        val spinnerPos = eventSpinner.selectedItemPosition

        // Name checks
        if (name.isEmpty()) { showMessage("Please enter your full name"); return }
        if (name.length < 3) { showMessage("Full name must be at least 3 characters"); return }
        if (name.any { it.isDigit() }) { showMessage("Full name must not contain digits"); return }

        // Phone checks
        if (phone.isEmpty()) { showMessage("Please enter your phone number"); return }
        if (!phone.matches(Regex("^\\d{10,11}$"))) { showMessage("Phone number must be 10-11 digits"); return }

        // Email checks
        if (email.isEmpty()) { showMessage("Please enter your email address"); return }
        if (!email.contains("@") || !email.contains(".")) { showMessage("Please enter a valid email address"); return }

        // Spinner check
        if (spinnerPos == 0) { showMessage("Please select an event type"); return }

        // Date checks
        if (!dateWasSelected) { showMessage("Please select an event date"); return }
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val pickedCal = Calendar.getInstance().apply {
            set(pickedYear, pickedMonth, pickedDay, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (pickedCal.before(todayCal)) { showMessage("Event date cannot be in the past"); return }

        // Gender check
        val genderSelectionId = genderGroup.checkedRadioButtonId
        if (genderSelectionId == -1) { showMessage("Please select your gender"); return }
        val genderText = findViewById<RadioButton>(genderSelectionId).text.toString()

        // Image check
        if (chosenPhotoUri == null) { showMessage("Please choose an image"); return }

        // Terms check
        if (!termsCheck.isChecked) { showMessage("Please accept the Terms and Conditions"); return }

        // Show confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Confirm Registration")
            .setMessage("Are you sure you want to submit your registration?")
            .setPositiveButton("Yes, Submit") { _, _ ->
                navigateToConfirmation(name, phone, email, eventCategory, genderText)
            }
            .setNegativeButton("Cancel") { dlg, _ -> dlg.dismiss() }
            .show()
    }

    private fun navigateToConfirmation(
        name: String, phone: String, email: String,
        eventCategory: String, gender: String
    ) {
        val confirmIntent = Intent(this@RegistrationActivity, ConfirmationActivity::class.java).apply {
            putExtra("EXTRA_NAME", name)
            putExtra("EXTRA_PHONE", phone)
            putExtra("EXTRA_EMAIL", email)
            putExtra("EXTRA_EVENT", eventCategory)
            putExtra("EXTRA_DATE", chosenDateText)
            putExtra("EXTRA_GENDER", gender)
            putExtra("EXTRA_PHOTO_URI", chosenPhotoUri.toString())
            clipData = ClipData.newRawUri("", chosenPhotoUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(confirmIntent)
    }
}
