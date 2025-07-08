package com.example.loginapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class CrearPrestamoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_prestamo, container, false)

        val editProducto = view.findViewById<EditText>(R.id.editProductoCrear)
        val editUsuario = view.findViewById<EditText>(R.id.editUsuarioCrear)
        val spinnerEstado = view.findViewById<Spinner>(R.id.spinnerEstado)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCrear)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.estados_prestamo,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstado.adapter = adapter
        }

        btnGuardar.setOnClickListener {
            val producto = editProducto.text.toString()
            val usuario = editUsuario.text.toString()
            val estado = spinnerEstado.selectedItem.toString()

            if (producto.isNotEmpty() && usuario.isNotEmpty()) {
                Toast.makeText(context, "Préstamo guardado: $producto", Toast.LENGTH_SHORT).show()
                editProducto.text.clear()
                editUsuario.text.clear()
            } else {
                Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}