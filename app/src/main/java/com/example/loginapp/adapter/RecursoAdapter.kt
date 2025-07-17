package com.example.loginapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.model.Recurso

class RecursoAdapter(
    private val lista: List<Recurso>,
    private val onItemClick: (Recurso) -> Unit,
    private val onDeleteClick: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNombre: TextView = view.findViewById(R.id.textNombreRecurso)
        val textDescripcion: TextView = view.findViewById(R.id.textDescripcionRecurso)
        val textCantidad: TextView = view.findViewById(R.id.textCantidadRecurso)
        val textEstado: TextView = view.findViewById(R.id.textEstadoRecurso)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarRecurso)

        init {
            view.setOnClickListener {
                val recurso = lista[adapterPosition]
                onItemClick(recurso)
            }

            btnEliminar.setOnClickListener {
                val recurso = lista[adapterPosition]
                onDeleteClick(recurso)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recurso = lista[position]
        holder.textNombre.text = recurso.nombre
        holder.textDescripcion.text = recurso.descripcion
        holder.textCantidad.text = "Cantidad: ${recurso.cantidad}"
        holder.textEstado.text = "Estado: ${recurso.estado}"

        val estadoLower = recurso.estado.lowercase()
        val context = holder.itemView.context

        // 🎨 Asignar color basado en el estado (usando solo colores blanco, negro, rojo)
        val colorResId = when (estadoLower) {
            "bueno" -> android.R.color.black
            "regular" -> android.R.color.darker_gray
            "deteriorado" -> android.R.color.holo_red_dark
            else -> android.R.color.black
        }

        holder.textEstado.setTextColor(ContextCompat.getColor(context, colorResId))
    }

    override fun getItemCount(): Int = lista.size
}