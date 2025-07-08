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

class CrearRolFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_rol, container, false)

        val editNombre = view.findViewById<EditText>(R.id.editNombreCrear)
        val editDescripcion = view.findViewById<EditText>(R.id.editDescripcionCrear)
        val spinnerPermisos = view.findViewById<Spinner>(R.id.spinnerPermisos)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCrear)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipos_permisos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPermisos.adapter = adapter
        }

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString()
            val descripcion = editDescripcion.text.toString()
            val permisos = spinnerPermisos.selectedItem.toString()

            if (nombre.isNotEmpty() && descripcion.isNotEmpty()) {
                Toast.makeText(context, "Rol guardado: $nombre", Toast.LENGTH_SHORT).show()
                editNombre.text.clear()
                editDescripcion.text.clear()
            } else {
                Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}