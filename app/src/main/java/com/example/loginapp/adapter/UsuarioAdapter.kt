package com.example.loginapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.FormularioUsuarioActivity
import com.example.loginapp.R
import com.example.loginapp.model.Usuario

class UsuarioAdapter(
    private val lista: List<Usuario>,
    private val onDeleteClick: (Usuario) -> Unit,
    private val onEditClick: ((Usuario) -> Unit)? = null
) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textNombre: TextView = view.findViewById(R.id.textNombre)
        private val textEmail: TextView = view.findViewById(R.id.textEmail)
        private val textTelefono: TextView = view.findViewById(R.id.textTelefono)
        private val textActivo: TextView = view.findViewById(R.id.textActivo)
        private val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarUsuario)

        init {
            view.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val usuario = lista[position]

                    // Si hay callback externo, úsalo
                    onEditClick?.invoke(usuario) ?: run {
                        val context = itemView.context
                        val intent = Intent(context, FormularioUsuarioActivity::class.java).apply {
                            putExtra("modo_edicion", true)
                            putExtra("usuario_id", usuario.id)
                            putExtra("usuario_nombre", usuario.nombre)
                            putExtra("usuario_apellido", usuario.apellido)
                            putExtra("usuario_email", usuario.email)
                            putExtra("usuario_telefono", usuario.telefono)
                            putExtra("usuario_rol", usuario.rol)
                            putExtra("usuario_activo", usuario.activo)
                            putExtra("usuario_password", usuario.password ?: "")
                        }
                        context.startActivity(intent)
                    }
                }
            }

            btnEliminar.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val usuario = lista[position]
                    onDeleteClick(usuario)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(usuario: Usuario) {
            textNombre.text = "${usuario.nombre} ${usuario.apellido}"
            textEmail.text = "Email: ${usuario.email}"
            textTelefono.text = "Teléfono: ${usuario.telefono}"
            textActivo.text = if (usuario.activo) "Activo" else "Inactivo"
            textActivo.setTextColor(itemView.context.getColor(
                if (usuario.activo) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            ))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size
}
