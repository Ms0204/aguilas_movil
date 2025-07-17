package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.adapter.RoleAdapter
import com.example.loginapp.model.MakyRole
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MakyRolesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var fabAgregarRol: FloatingActionButton

    // ✅ Launcher para detectar regreso del formulario
    private val formularioRolLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            cargarRoles() // 🔄 Recarga al volver
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maky_roles)

        recyclerView = findViewById(R.id.recyclerViewRoles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        fabAgregarRol = findViewById(R.id.fabAgregarRol)
        fabAgregarRol.setOnClickListener {
            val intent = Intent(this, FormularioRolActivity::class.java)
            intent.putExtra("modo_edicion", false)
            formularioRolLauncher.launch(intent)
        }

        cargarRoles()
    }

    private fun cargarRoles() {
        lifecycleScope.launch {
            try {
                val roles: List<MakyRole> = RetrofitClient.instance.getRoles()

                recyclerView.adapter = RoleAdapter(
                    lista = roles,
                    onItemClick = { rolSeleccionado ->
                        val intent = Intent(this@MakyRolesActivity, FormularioRolActivity::class.java).apply {
                            putExtra("modo_edicion", true)
                            putExtra("id", rolSeleccionado.id)
                            putExtra("name", rolSeleccionado.name)
                            putExtra("descripcion", rolSeleccionado.descripcion)
                        }
                        formularioRolLauncher.launch(intent)
                    },
                    onDeleteClick = { rolAEliminar ->
                        confirmarEliminacion(rolAEliminar)
                    }
                )

            } catch (e: Exception) {
                Log.e("MakyRoles", "Error al obtener roles", e)
                Toast.makeText(this@MakyRolesActivity, "Error al cargar roles", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun confirmarEliminacion(rol: MakyRole) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar rol")
            .setMessage("¿Deseas eliminar el rol \"${rol.name}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarRol(rol.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarRol(idRol: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.instance.deleteRole(idRol)
                Toast.makeText(this@MakyRolesActivity, "Rol eliminado", Toast.LENGTH_SHORT).show()
                cargarRoles()
            } catch (e: Exception) {
                Log.e("EliminarRol", "Error eliminando rol", e)
                Toast.makeText(this@MakyRolesActivity, "Error al eliminar rol", Toast.LENGTH_LONG).show()
            }
        }
    }
}
