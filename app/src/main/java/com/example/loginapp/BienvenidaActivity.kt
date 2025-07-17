package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        // 🔐 Leer datos guardados de sesión
        val prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        val nombreCompleto = prefs.getString("USERNAME", "Usuario")
        val rolUsuario = prefs.getString("ROL", "Rol no definido")

        // 👋 Mostrar saludo en la interfaz
        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenida)
        val txtRol = findViewById<TextView>(R.id.txtRol)
        txtBienvenida.text = "¡Hola, $nombreCompleto!"
        txtRol.text = "Rol: $rolUsuario"

        // 🔗 Botones para navegación modular
        findViewById<CardView>(R.id.btnUsuarios).setOnClickListener {
            startActivity(Intent(this, MakyUsuariosActivity::class.java))
        }

        findViewById<CardView>(R.id.btnPrestamos).setOnClickListener {
            startActivity(Intent(this, MakyPrestamosActivity::class.java))
        }

        findViewById<CardView>(R.id.btnProductos).setOnClickListener {
            startActivity(Intent(this, ProductoActivity::class.java))
        }

        findViewById<CardView>(R.id.btnRecursos).setOnClickListener {
            startActivity(Intent(this, MakyRecursosActivity::class.java))
        }

        findViewById<CardView>(R.id.btnRoles).setOnClickListener {
            startActivity(Intent(this, MakyRolesActivity::class.java))
        }

        // 🚪 Cerrar sesión y volver al login
        findViewById<CardView>(R.id.btnCerrarSesion).setOnClickListener {
            prefs.edit().clear().apply()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("showLogin", true)
            startActivity(intent)
        }
    }
}
