package com.example.loginapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.model.MakyRole

class RoleAdapter(
    private val lista: List<MakyRole>,
    private val onItemClick: (MakyRole) -> Unit,
    private val onDeleteClick: (MakyRole) -> Unit
) : RecyclerView.Adapter<RoleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNombreRol: TextView = view.findViewById(R.id.textNombreRol)
        val textDescripcionRol: TextView = view.findViewById(R.id.textDescripcionRol)
        val btnEliminarRol: ImageButton = view.findViewById(R.id.btnEliminarRol)

        init {
            view.setOnClickListener {
                val rol = lista[adapterPosition]
                onItemClick(rol)
            }
            btnEliminarRol.setOnClickListener {
                val rol = lista[adapterPosition]
                onDeleteClick(rol)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_role, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rol = lista[position]
        holder.textNombreRol.text = rol.name  // ✅ Antes era 'rol.nombre'
        holder.textDescripcionRol.text = "Descripción: ${rol.descripcion}"
    }

    override fun getItemCount(): Int = lista.size
}
