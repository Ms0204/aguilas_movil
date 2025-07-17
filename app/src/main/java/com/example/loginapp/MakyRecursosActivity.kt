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
import com.example.loginapp.adapter.RecursoAdapter
import com.example.loginapp.model.Recurso
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MakyRecursosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var fabAgregarRecurso: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maky_recursos)

        recyclerView = findViewById(R.id.recyclerViewRecursos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        fabAgregarRecurso = findViewById(R.id.fabAgregarRecurso)
        fabAgregarRecurso.setOnClickListener {
            val intent = Intent(this, FormularioRecursoActivity::class.java)
            intent.putExtra("modo_edicion", false)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarRecursos()
    }

    private fun cargarRecursos() {
        lifecycleScope.launch {
            try {
                val recursos: List<Recurso> = RetrofitClient.instance.getRecursos()
                recyclerView.adapter = RecursoAdapter(
                    lista = recursos,
                    onItemClick = { recurso ->
                        val intent = Intent(this@MakyRecursosActivity, FormularioRecursoActivity::class.java).apply {
                            putExtra("modo_edicion", true)
                            putExtra("id", recurso.id)
                            putExtra("nombre", recurso.nombre)
                            putExtra("descripcion", recurso.descripcion)
                            putExtra("cantidad", recurso.cantidad)
                            putExtra("estado", recurso.estado) // Puede ser Bueno, Regular, Deteriorado
                        }
                        startActivity(intent)
                    },
                    onDeleteClick = { recurso ->
                        confirmarEliminacion(recurso)
                    }
                )
            } catch (e: Exception) {
                Log.e("MakyRecursos", "Error al obtener recursos", e)
                Toast.makeText(this@MakyRecursosActivity, "Error al cargar los recursos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun confirmarEliminacion(recurso: Recurso) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar recurso")
            .setMessage("¿Estás seguro que deseas eliminar \"${recurso.nombre}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarRecurso(recurso.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarRecurso(id: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.instance.deleteRecurso(id)
                Toast.makeText(this@MakyRecursosActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                cargarRecursos()
            } catch (e: Exception) {
                Log.e("EliminarRecurso", "Error eliminando recurso", e)
                Toast.makeText(this@MakyRecursosActivity, "Error al eliminar recurso", Toast.LENGTH_LONG).show()
            }
        }
    }
}