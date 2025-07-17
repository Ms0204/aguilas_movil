package com.example.loginapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.model.Producto
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FormularioProductoActivity : AppCompatActivity() {

    private var productoId: Int? = null
    private val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_producto)

        val etTitulo = findViewById<TextView>(R.id.tituloFormularioProducto)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val etFechaEntrada = findViewById<EditText>(R.id.etFechaEntrada)
        val etFechaSalida = findViewById<EditText>(R.id.etFechaSalida)
        val etCantidad = findViewById<EditText>(R.id.etCantidad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        val estados = listOf("Disponible", "Agotado")
        spEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)

        etFechaEntrada.setOnClickListener {
            mostrarDatePicker { fecha -> etFechaEntrada.setText(fecha) }
        }
        etFechaSalida.setOnClickListener {
            mostrarDatePicker { fecha -> etFechaSalida.setText(fecha) }
        }

        val producto = intent.getSerializableExtra("producto") as? Producto
        producto?.let {
            productoId = it.id
            etTitulo.text = "Editar producto"
            etNombre.setText(it.nombre)
            spEstado.setSelection(estados.indexOf(it.estado))
            etFechaEntrada.setText(it.fecha_entrada)
            etFechaSalida.setText(it.fecha_salida)
            etCantidad.setText(it.cantidad.toString())
            btnGuardar.text = "Actualizar"
        } ?: run {
            etTitulo.text = "Crear nuevo producto"
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val estado = spEstado.selectedItem.toString()
            val fechaEntrada = etFechaEntrada.text.toString().trim()
            val fechaSalida = etFechaSalida.text.toString().trim()
            val cantidadTexto = etCantidad.text.toString().trim()

            if (nombre.isEmpty() || fechaEntrada.isEmpty() || fechaSalida.isEmpty() || cantidadTexto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadTexto.toIntOrNull()
            if (cantidad == null || cantidad < 0) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha1 = formatoFecha.parse(fechaEntrada)
            val fecha2 = formatoFecha.parse(fechaSalida)
            if (fecha2.before(fecha1)) {
                Toast.makeText(this, "La fecha de salida no puede ser anterior a la de entrada", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val nuevoProducto = Producto(
                id = productoId,
                nombre = nombre,
                estado = estado,
                fecha_entrada = fechaEntrada,
                fecha_salida = fechaSalida,
                cantidad = cantidad
            )

            lifecycleScope.launch {
                try {
                    val mensaje = if (productoId == null) {
                        RetrofitClient.instance.crearProducto(nuevoProducto)
                        "Producto creado correctamente"
                    } else {
                        RetrofitClient.instance.actualizarProducto(productoId!!, nuevoProducto)
                        "Producto actualizado correctamente"
                    }
                    Toast.makeText(this@FormularioProductoActivity, mensaje, Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@FormularioProductoActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mostrarDatePicker(onDateSelected: (String) -> Unit) {
        val calendario = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = Calendar.getInstance()
                fecha.set(year, month, dayOfMonth)
                onDateSelected(formatoFecha.format(fecha.time))
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
