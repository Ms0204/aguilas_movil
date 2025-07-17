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
import com.example.loginapp.adapter.UsuarioAdapter
import com.example.loginapp.model.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MakyUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var fabAgregarUsuario: FloatingActionButton
    private lateinit var adapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maky_usuarios)

        recyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        fabAgregarUsuario = findViewById(R.id.fabAgregarUsuario)
        fabAgregarUsuario.setOnClickListener {
            val intent = Intent(this, FormularioUsuarioActivity::class.java)
            intent.putExtra("modo_edicion", false)
            startActivity(intent)
        }

        cargarUsuarios()
    }

    override fun onResume() {
        super.onResume()
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        lifecycleScope.launch {
            try {
                val usuarios = RetrofitClient.instance.getUsuarios()
                adapter = UsuarioAdapter(
                    lista = usuarios,
                    onDeleteClick = { usuario -> confirmarEliminacion(usuario) },
                    onEditClick = { usuario -> abrirFormularioEdicion(usuario) }
                )
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("MakyUsuarios", "Error al obtener usuarios", e)
                Toast.makeText(this@MakyUsuariosActivity, "Error al cargar usuarios", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun confirmarEliminacion(usuario: Usuario) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar usuario")
            .setMessage("¿Deseas eliminar a \"${usuario.nombre} ${usuario.apellido}\"?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarUsuario(usuario.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarUsuario(id: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.instance.eliminarUsuario(id)
                Toast.makeText(this@MakyUsuariosActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                cargarUsuarios()
            } catch (e: Exception) {
                Log.e("EliminarUsuario", "Error al eliminar usuario", e)
                Toast.makeText(this@MakyUsuariosActivity, "Error al eliminar usuario", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun abrirFormularioEdicion(usuario: Usuario) {
        val intent = Intent(this, FormularioUsuarioActivity::class.java)
        intent.putExtra("modo_edicion", true)
        intent.putExtra("usuario_id", usuario.id)
        intent.putExtra("usuario_nombre", usuario.nombre)
        intent.putExtra("usuario_apellido", usuario.apellido)
        intent.putExtra("usuario_email", usuario.email)
        intent.putExtra("usuario_telefono", usuario.telefono)
        intent.putExtra("usuario_rol", usuario.rol)
        intent.putExtra("usuario_activo", usuario.activo)
        intent.putExtra("usuario_password", usuario.password) // si lo tienes
        startActivity(intent)
    }
}