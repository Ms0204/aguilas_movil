package com.example.loginapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.databinding.ActivityProductoBinding
import kotlinx.coroutines.launch

class ProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 📋 Configuración del listado
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)

        // 📝 Título superior
        val titulo = findViewById<TextView>(R.id.tituloGestion)
        titulo.text = "📦 Gestión de Productos"

        // 🔙 Botón de volver
        findViewById<ImageButton>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        // ➕ Crear nuevo producto
        binding.fabAgregarProducto.setOnClickListener {
            startActivity(Intent(this, FormularioProductoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        lifecycleScope.launch {
            try {
                val productos = RetrofitClient.instance.getProductos().toMutableList()
                binding.recyclerViewProductos.adapter = ProductoAdapter(productos) {
                    cargarProductos()
                }
            } catch (e: Exception) {
                Log.e("ProductoActivity", "Error al cargar productos: ${e.message}")
            }
        }
    }
}