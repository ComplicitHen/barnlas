package com.barnlas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UnlockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock)

        val pinInput = findViewById<EditText>(R.id.editUnlockPin)
        val unlockBtn = findViewById<Button>(R.id.btnUnlock)
        val cancelBtn = findViewById<Button>(R.id.btnCancel)

        unlockBtn.setOnClickListener {
            val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
            val correctPin = prefs.getString(MainActivity.KEY_PIN, MainActivity.DEFAULT_PIN)

            if (pinInput.text.toString() == correctPin) {
                stopService(Intent(this, LockService::class.java))
                finish()
            } else {
                Toast.makeText(this, "Fel PIN, forsok igen", Toast.LENGTH_SHORT).show()
                pinInput.text.clear()
            }
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
