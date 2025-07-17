package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.adapter.PrestamoAdapter
import com.example.loginapp.model.Prestamo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MakyPrestamosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var fabAgregarPrestamo: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maky_prestamos)

        recyclerView = findViewById(R.id.recyclerViewPrestamos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        fabAgregarPrestamo = findViewById(R.id.fabAgregarPrestamo)
        fabAgregarPrestamo.setOnClickListener {
            val intent = Intent(this, FormularioPrestamoActivity::class.java)
            intent.putExtra("modo_edicion", false)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarPrestamos()
    }

    private fun cargarPrestamos() {
        lifecycleScope.launch {
            try {
                val prestamos: List<Prestamo> = RetrofitClient.instance.getPrestamos()
                recyclerView.adapter = PrestamoAdapter(
                    lista = prestamos,
                    onDeleteClick = { prestamo -> confirmarEliminacion(prestamo) }
                )
            } catch (e: Exception) {
                Log.e("MakyPrestamos", "Error al obtener préstamos", e)
                Toast.makeText(this@MakyPrestamosActivity, "Error al cargar los préstamos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun confirmarEliminacion(prestamo: Prestamo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar préstamo")
            .setMessage("¿Deseas eliminar el préstamo con código \"${prestamo.codigo}\"?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarPrestamo(prestamo.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarPrestamo(id: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.instance.eliminarPrestamo(id)
                Toast.makeText(this@MakyPrestamosActivity, "Préstamo eliminado", Toast.LENGTH_SHORT).show()
                cargarPrestamos()
            } catch (e: Exception) {
                Log.e("EliminarPrestamo", "Error al eliminar préstamo", e)
                Toast.makeText(this@MakyPrestamosActivity, "Error al eliminar préstamo", Toast.LENGTH_LONG).show()
            }
        }
    }
}