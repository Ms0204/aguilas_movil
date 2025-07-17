package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.model.MakyRole
import kotlinx.coroutines.launch

class FormularioRolActivity : AppCompatActivity() {

    private lateinit var editNombreRol: EditText
    private lateinit var editDescripcionRol: EditText
    private lateinit var btnGuardarRol: Button
    private lateinit var txtTituloFormularioRol: TextView

    private var modoEdicion = false
    private var rolId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_rol)

        editNombreRol = findViewById(R.id.editNombreRol)
        editDescripcionRol = findViewById(R.id.editDescripcionRol)
        btnGuardarRol = findViewById(R.id.btnGuardarRol)
        txtTituloFormularioRol = findViewById(R.id.txtTituloFormularioRol)

        // 🧠 Detecta si es edición y precarga los datos
        modoEdicion = intent.getBooleanExtra("modo_edicion", false)

        if (modoEdicion) {
            txtTituloFormularioRol.text = "Editar Rol"
            btnGuardarRol.text = "Actualizar Rol"

            rolId = intent.getIntExtra("id", 0)
            val name = intent.getStringExtra("name") ?: ""
            val descripcion = intent.getStringExtra("descripcion") ?: ""

            editNombreRol.setText(name)
            editDescripcionRol.setText(descripcion)
        } else {
            txtTituloFormularioRol.text = "Crear Rol"
            btnGuardarRol.text = "Guardar Rol"
        }

        btnGuardarRol.setOnClickListener {
            val name = editNombreRol.text.toString().trim()
            val descripcion = editDescripcionRol.text.toString().trim()

            if (name.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rol = MakyRole(id = rolId, name = name, descripcion = descripcion)

            lifecycleScope.launch {
                try {
                    if (modoEdicion) {
                        RetrofitClient.instance.actualizarRol(rolId, rol)
                        Toast.makeText(this@FormularioRolActivity, "Rol actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        RetrofitClient.instance.createRole(rol)
                        Toast.makeText(this@FormularioRolActivity, "Rol creado exitosamente", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@FormularioRolActivity, "Error al guardar rol", Toast.LENGTH_LONG).show()
                    Log.e("FormularioRol", "Error", e)
                }
            }
        }
    }
}
