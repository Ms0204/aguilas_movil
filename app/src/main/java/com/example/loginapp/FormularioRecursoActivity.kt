package com.example.loginapp

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.model.Recurso
import kotlinx.coroutines.launch

class FormularioRecursoActivity : AppCompatActivity() {

    private lateinit var txtTitulo: TextView
    private lateinit var editNombre: EditText
    private lateinit var editDescripcion: EditText
    private lateinit var editCantidad: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnGuardar: Button

    private var modoEdicion = false
    private var recursoId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_recurso)

        txtTitulo = findViewById(R.id.txtTituloFormularioRecurso)
        editNombre = findViewById(R.id.editNombreRecurso)
        editDescripcion = findViewById(R.id.editDescripcionRecurso)
        editCantidad = findViewById(R.id.editCantidadRecurso)
        spinnerEstado = findViewById(R.id.spinnerEstadoRecurso)
        btnGuardar = findViewById(R.id.btnGuardarRecurso)

        configurarSpinnerEstado()

        modoEdicion = intent.getBooleanExtra("modo_edicion", false)
        if (modoEdicion) {
            recursoId = intent.getIntExtra("id", 0)
            editNombre.setText(intent.getStringExtra("nombre"))
            editDescripcion.setText(intent.getStringExtra("descripcion"))
            editCantidad.setText(intent.getIntExtra("cantidad", 0).toString())

            val estado = intent.getStringExtra("estado") ?: "Bueno"
            val index = when (estado.lowercase()) {
                "bueno" -> 0
                "regular" -> 1
                "deteriorado" -> 2
                else -> 0
            }
            spinnerEstado.setSelection(index)

            txtTitulo.text = "Editar Recurso"
            btnGuardar.text = "Actualizar Recurso"
        } else {
            txtTitulo.text = "Crear Recurso"
            btnGuardar.text = "Guardar Recurso"
        }

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val descripcion = editDescripcion.text.toString().trim()
            val cantidadStr = editCantidad.text.toString().trim()
            val estado = spinnerEstado.selectedItem.toString()

            if (nombre.isEmpty() || descripcion.isEmpty() || cantidadStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toIntOrNull()
            if (cantidad == null || cantidad < 0) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recurso = Recurso(
                id = recursoId,
                nombre = nombre,
                descripcion = descripcion,
                cantidad = cantidad,
                estado = estado
            )

            lifecycleScope.launch {
                try {
                    if (modoEdicion) {
                        RetrofitClient.instance.actualizarRecurso(recursoId, recurso)
                        Toast.makeText(this@FormularioRecursoActivity, "Recurso actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        RetrofitClient.instance.createRecurso(recurso)
                        Toast.makeText(this@FormularioRecursoActivity, "Recurso creado", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                } catch (e: Exception) {
                    Log.e("FormularioRecurso", "Error guardando recurso", e)
                    Toast.makeText(this@FormularioRecursoActivity, "Error al guardar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun configurarSpinnerEstado() {
        val opciones = listOf("Bueno", "Regular", "Deteriorado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinnerEstado.adapter = adapter
    }
}