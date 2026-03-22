package com.barnlas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "barnlas_prefs"
        const val KEY_PIN = "pin"
        const val DEFAULT_PIN = "1234"
        private const val OVERLAY_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val pinEdit = findViewById<EditText>(R.id.editPin)
        val savePinBtn = findViewById<Button>(R.id.btnSavePin)
        val lockBtn = findViewById<Button>(R.id.btnLock)

        pinEdit.setText(prefs.getString(KEY_PIN, DEFAULT_PIN))

        savePinBtn.setOnClickListener {
            val pin = pinEdit.text.toString().trim()
            if (pin.length >= 4 && pin.all { it.isDigit() }) {
                prefs.edit().putString(KEY_PIN, pin).apply()
                Toast.makeText(this, "PIN sparad!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "PIN maste vara minst 4 siffror", Toast.LENGTH_SHORT).show()
            }
        }

        lockBtn.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                startForegroundService(Intent(this, LockService::class.java))
                moveTaskToBack(true)
            } else {
                startActivityForResult(
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),
                    OVERLAY_REQUEST
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_REQUEST && Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Behorighet ok! Tryck Aktivera igen.", Toast.LENGTH_SHORT).show()
        }
    }
}
