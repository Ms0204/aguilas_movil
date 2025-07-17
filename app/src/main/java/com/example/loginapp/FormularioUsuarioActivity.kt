package com.example.loginapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.api.RetrofitClient
import com.example.loginapp.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Response

class FormularioUsuarioActivity : AppCompatActivity() {

    private lateinit var tituloFormulario: TextView
    private lateinit var editNombre: TextInputEditText
    private lateinit var editApellido: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editTelefono: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var spinnerActivo: Spinner
    private lateinit var spinnerRol: Spinner
    private lateinit var btnGuardar: Button

    private var modoEdicion = false
    private var usuarioId = 0

    private val rolesInternos = listOf("usuario", "admin") // Laravel espera estos nombres
    private val rolesVisuales = listOf("Usuario", "Administrador")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_usuario)

        tituloFormulario = findViewById(R.id.titleEditarUsuario)
        editNombre = findViewById(R.id.editNombreUsuario)
        editApellido = findViewById(R.id.editApellidoUsuario)
        editEmail = findViewById(R.id.editEmailUsuario)
        editTelefono = findViewById(R.id.editTelefonoUsuario)
        editPassword = findViewById(R.id.editPasswordUsuario)
        spinnerActivo = findViewById(R.id.spinnerActivoUsuario)
        spinnerRol = findViewById(R.id.spinnerRolUsuario)
        btnGuardar = findViewById(R.id.btnGuardarUsuario)

        configurarSpinners()

        modoEdicion = intent.getBooleanExtra("modo_edicion", false)
        if (modoEdicion) {
            usuarioId = intent.getIntExtra("usuario_id", 0)
            editNombre.setText(intent.getStringExtra("usuario_nombre"))
            editApellido.setText(intent.getStringExtra("usuario_apellido"))
            editEmail.setText(intent.getStringExtra("usuario_email"))
            editTelefono.setText(intent.getStringExtra("usuario_telefono"))
            editPassword.setHint("Nueva contraseña (opcional)")

            val rolRecibido = intent.getStringExtra("usuario_rol")?.lowercase() ?: "usuario"
            val activo = intent.getBooleanExtra("usuario_activo", true)

            spinnerActivo.setSelection(if (activo) 0 else 1)
            val indexRol = rolesInternos.indexOf(rolRecibido)
            spinnerRol.setSelection(if (indexRol >= 0) indexRol else 0)

            tituloFormulario.text = "Editar Usuario"
            btnGuardar.text = "Actualizar usuario"
        } else {
            tituloFormulario.text = "Crear Usuario"
            btnGuardar.text = "Guardar usuario"
        }

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val apellido = editApellido.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val telefono = editTelefono.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val activo = spinnerActivo.selectedItem.toString().lowercase() == "activo"
            val rol = rolesInternos[spinnerRol.selectedItemPosition]

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!modoEdicion && password.isEmpty()) {
                Toast.makeText(this, "La contraseña es obligatoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val datos = mutableMapOf<String, Any>(
                "nombre" to nombre,
                "apellido" to apellido,
                "email" to email,
                "telefono" to telefono,
                "activo" to activo,
                "roles" to listOf(rol)
            )

            if (!modoEdicion || password.isNotEmpty()) {
                datos["password"] = password
            }

            lifecycleScope.launch {
                val response: Response<Usuario> = try {
                    if (modoEdicion) {
                        RetrofitClient.instance.actualizarUsuario(usuarioId, datos)
                    } else {
                        RetrofitClient.instance.crearUsuario(datos)
                    }
                } catch (e: Exception) {
                    Log.e("FormularioUsuario", "Error de red: ${e.message}")
                    Toast.makeText(this@FormularioUsuarioActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    return@launch
                }

                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("UsuarioDebug", "Guardado: ${usuario?.id} - ${usuario?.email}")
                    Toast.makeText(this@FormularioUsuarioActivity, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("FormularioUsuario", "Error ${response.code()}: $error")
                    Toast.makeText(this@FormularioUsuarioActivity, "Error: $error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun configurarSpinners() {
        val estados = listOf("Activo", "Inactivo")
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        spinnerActivo.adapter = adapterEstado

        val adapterRoles = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rolesVisuales)
        spinnerRol.adapter = adapterRoles
    }
}