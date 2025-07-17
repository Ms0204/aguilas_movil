package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Referencias a los componentes del nuevo layout
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        // Acción del botón "Enviar"
        sendButton.setOnClickListener {
            val email = emailInput.text.toString().trim().lowercase()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu correo.", Toast.LENGTH_SHORT).show()
            } else {
                // Aquí puedes integrar la llamada al backend si deseas
                Toast.makeText(
                    this,
                    "Se ha enviado correctamente el enlace de recuperación a $email",
                    Toast.LENGTH_LONG
                ).show()

                emailInput.text?.clear()
            }
        }
    }
}