package com.example.loginapp

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.api.RetrofitClient
import com.example.loginapp.model.Prestamo
import com.example.loginapp.model.Recurso
import com.example.loginapp.model.Usuario
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class FormularioPrestamoActivity : AppCompatActivity() {

    private lateinit var tituloFormulario: TextView
    private lateinit var editCodigo: EditText
    private lateinit var spinnerUsuario: Spinner
    private lateinit var spinnerRecurso: Spinner
    private lateinit var editFechaPrestamo: EditText
    private lateinit var editFechaDevolucion: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var grupoFechaDevolucion: LinearLayout

    private var modoEdicion = false
    private var prestamoId = 0
    private lateinit var listaUsuarios: List<Usuario>
    private lateinit var listaRecursos: List<Recurso>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_prestamo)

        tituloFormulario = findViewById(R.id.titleFormularioPrestamo)
        editCodigo = findViewById(R.id.editCodigoPrestamo)
        spinnerUsuario = findViewById(R.id.spinnerUsuario)
        spinnerRecurso = findViewById(R.id.spinnerRecurso)
        editFechaPrestamo = findViewById(R.id.editFechaPrestamo)
        editFechaDevolucion = findViewById(R.id.editFechaDevolucion)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnGuardar = findViewById(R.id.btnGuardarPrestamo)
        grupoFechaDevolucion = findViewById(R.id.grupoFechaDevolucion)

        editFechaPrestamo.setOnClickListener { showDatePickerDialog(editFechaPrestamo) }
        editFechaDevolucion.setOnClickListener { showDatePickerDialog(editFechaDevolucion) }

        val estados = listOf("Pendiente", "Devuelto", "No devuelto")
        spinnerEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)

        spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val estadoSeleccionado = estados[position].lowercase()
                grupoFechaDevolucion.visibility = if (estadoSeleccionado == "devuelto") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        lifecycleScope.launch {
            try {
                listaUsuarios = RetrofitClient.instance.getUsuarios()
                listaRecursos = RetrofitClient.instance.getRecursos()

                spinnerUsuario.adapter = ArrayAdapter(this@FormularioPrestamoActivity, android.R.layout.simple_spinner_dropdown_item, listaUsuarios.map { "${it.nombre} ${it.apellido}" })
                spinnerRecurso.adapter = ArrayAdapter(this@FormularioPrestamoActivity, android.R.layout.simple_spinner_dropdown_item, listaRecursos.map { it.nombre })

                modoEdicion = intent.getBooleanExtra("modo_edicion", false)
                if (modoEdicion) {
                    prestamoId = intent.getIntExtra("id", 0)
                    tituloFormulario.text = "Editar Préstamo"
                    btnGuardar.text = "Actualizar préstamo"

                    editCodigo.setText(intent.getStringExtra("codigo") ?: "")
                    editFechaPrestamo.setText(intent.getStringExtra("fecha_prestamo") ?: "")
                    editFechaDevolucion.setText(intent.getStringExtra("fecha_devolucion") ?: "")
                    val estado = intent.getStringExtra("estado") ?: "Pendiente"
                    spinnerEstado.setSelection(estados.indexOfFirst { it.equals(estado, ignoreCase = true) })

                    val usuarioId = intent.getIntExtra("usuario_id", 0)
                    val recursoId = intent.getIntExtra("recurso_id", 0)
                    spinnerUsuario.setSelection(listaUsuarios.indexOfFirst { it.id == usuarioId }.coerceAtLeast(0))
                    spinnerRecurso.setSelection(listaRecursos.indexOfFirst { it.id == recursoId }.coerceAtLeast(0))
                } else {
                    tituloFormulario.text = "Registrar Préstamo"
                    btnGuardar.text = "Guardar préstamo"
                }

            } catch (e: Exception) {
                Log.e("FormularioPrestamo", "Error al cargar datos", e)
                Toast.makeText(this@FormularioPrestamoActivity, "Error al cargar usuarios o recursos", Toast.LENGTH_LONG).show()
            }
        }

        btnGuardar.setOnClickListener {
            val codigoManual = editCodigo.text.toString().trim()
            val fechaPrestamo = editFechaPrestamo.text.toString().trim()
            var fechaDevolucion = editFechaDevolucion.text.toString().trim()
            val estado = spinnerEstado.selectedItem.toString().lowercase()

            val usuario = listaUsuarios.getOrNull(spinnerUsuario.selectedItemPosition)
            val recurso = listaRecursos.getOrNull(spinnerRecurso.selectedItemPosition)

            if (fechaPrestamo.isEmpty() || usuario == null || recurso == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (estado == "devuelto") {
                if (fechaDevolucion.isEmpty()) {
                    Toast.makeText(this, "Completa la fecha de devolución", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                try {
                    val prestamoDate = LocalDate.parse(fechaPrestamo)
                    val devolucionDate = LocalDate.parse(fechaDevolucion)
                    if (devolucionDate.isBefore(prestamoDate)) {
                        Toast.makeText(this, "La devolución no puede ser antes del préstamo", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                fechaDevolucion = null.toString()
            }

            val codigoFinal = if (codigoManual.isNotEmpty()) codigoManual else "${recurso.nombre}-${fechaPrestamo.replace("/", "-")}"

            val prestamo = Prestamo(
                id = prestamoId,
                codigo = codigoFinal,
                usuario_id = usuario.id,
                recurso_id = recurso.id,
                fecha_prestamo = fechaPrestamo,
                fecha_devolucion = fechaDevolucion,
                estado = estado
            )

            lifecycleScope.launch {
                try {
                    val response = if (modoEdicion) {
                        RetrofitClient.instance.actualizarPrestamo(prestamoId, prestamo)
                    } else {
                        RetrofitClient.instance.crearPrestamo(prestamo)
                    }

                    if (response.isSuccessful) {
                        val mensaje = if (modoEdicion) "Préstamo actualizado correctamente" else "Préstamo registrado correctamente"
                        Toast.makeText(this@FormularioPrestamoActivity, mensaje, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("PrestamoERROR", "Código: ${response.code()} — $errorBody")
                        Toast.makeText(this@FormularioPrestamoActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    Log.e("PrestamoExcepcion", "Error inesperado al guardar préstamo", e)
                    Toast.makeText(this@FormularioPrestamoActivity, "Error grave: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDatePickerDialog(campo: EditText) {
        val calendario = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val fecha = "%04d-%02d-%02d".format(year, month + 1, day)
                campo.setText(fecha)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}