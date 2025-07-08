package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Obtener las referencias de los campos con los nuevos IDs
        val emailEditText = findViewById<EditText>(R.id.emailForRecoveryEditText)
        val sendEmailButton = findViewById<Button>(R.id.submitRecoveryButton)
        val backToLoginTextView = findViewById<TextView>(R.id.backToLoginTextView)

        // Funcionalidad del botón Enviar
        sendEmailButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa un correo electrónico.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulación del envío
            Toast.makeText(
                this,
                "Instrucciones para recuperar la contraseña han sido enviadas a $email.",
                Toast.LENGTH_LONG
            ).show()

            emailEditText.text.clear()
        }

        // Volver a la pantalla de login
        backToLoginTextView.setOnClickListener {
            finish()
        }
    }
}