package com.example.loginapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class CrearFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear, container, false)

        val editNombre = view.findViewById<EditText>(R.id.editNombreCrear)
        val editDescripcion = view.findViewById<EditText>(R.id.editDescripcionCrear)
        val spinnerEstado = view.findViewById<Spinner>(R.id.spinnerEstado)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCrear)

        // Configurar el Spinner
        val estados = listOf("Activo", "Inactivo")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val descripcion = editDescripcion.text.toString().trim()
            val estadoSeleccionado = spinnerEstado.selectedItem.toString()

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Producto creado: $nombre\nEstado: $estadoSeleccionado",
                    Toast.LENGTH_SHORT
                ).show()

                // Limpia los campos
                editNombre.text.clear()
                editDescripcion.text.clear()
                spinnerEstado.setSelection(0)
            }
        }

        return view
    }
}