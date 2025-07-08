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

class CrearUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_usuario, container, false)

        val editNombre = view.findViewById<EditText>(R.id.editNombreCrear)
        val editEmail = view.findViewById<EditText>(R.id.editEmailCrear)
        val spinnerTipo = view.findViewById<Spinner>(R.id.spinnerTipoUsuario)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCrear)

        // Configurar spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipos_usuario,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipo.adapter = adapter
        }

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString()
            val email = editEmail.text.toString()
            val tipo = spinnerTipo.selectedItem.toString()

            if (nombre.isNotEmpty() && email.isNotEmpty()) {
                Toast.makeText(context, "Usuario guardado: $nombre", Toast.LENGTH_SHORT).show()
                editNombre.text.clear()
                editEmail.text.clear()
            } else {
                Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}