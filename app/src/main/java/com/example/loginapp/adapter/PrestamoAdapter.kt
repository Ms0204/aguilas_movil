package com.example.loginapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.FormularioPrestamoActivity
import com.example.loginapp.R
import com.example.loginapp.model.Prestamo

class PrestamoAdapter(
    private var lista: List<Prestamo>,
    private val onDeleteClick: (Prestamo) -> Unit
) : RecyclerView.Adapter<PrestamoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textCodigo: TextView = view.findViewById(R.id.textCodigo)
        private val textUsuario: TextView = view.findViewById(R.id.textUsuario)
        private val textRecurso: TextView = view.findViewById(R.id.textRecurso)
        private val textFechas: TextView = view.findViewById(R.id.textFechas)
        private val textEstado: TextView = view.findViewById(R.id.textEstado)
        private val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarPrestamo)

        init {
            view.setOnClickListener {
                val prestamo = lista[bindingAdapterPosition]
                val context = itemView.context
                val intent = Intent(context, FormularioPrestamoActivity::class.java).apply {
                    putExtra("modo_edicion", true)
                    putExtra("id", prestamo.id)
                    putExtra("fecha_prestamo", prestamo.fecha_prestamo)
                    putExtra("fecha_devolucion", prestamo.fecha_devolucion)
                    putExtra("estado", prestamo.estado)
                    putExtra("usuario_id", prestamo.usuario_id)
                    putExtra("recurso_id", prestamo.recurso_id)
                }
                context.startActivity(intent)
            }

            btnEliminar.setOnClickListener {
                val prestamo = lista[bindingAdapterPosition]
                onDeleteClick(prestamo)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(prestamo: Prestamo) {
            textCodigo.text = "Código: ${prestamo.codigo}"
            textUsuario.text = "Usuario: ${prestamo.usuario?.nombre ?: "ID ${prestamo.usuario_id}"}"
            textRecurso.text = "Recurso: ${prestamo.recurso?.nombre ?: "ID ${prestamo.recurso_id}"}"

            val fechasTexto = if (prestamo.estado.equals("devuelto", ignoreCase = true)) {
                val devolucion = if (prestamo.fecha_devolucion.isBlank()) "—" else prestamo.fecha_devolucion
                "Préstamo: ${prestamo.fecha_prestamo} | Devolución: $devolucion"
            } else {
                "Préstamo: ${prestamo.fecha_prestamo}"
            }

            textFechas.text = fechasTexto
            textEstado.text = "Estado: ${prestamo.estado}"

            val color = when (prestamo.estado.lowercase()) {
                "devuelto" -> android.R.color.holo_green_dark
                "pendiente" -> android.R.color.holo_orange_dark
                "no devuelto" -> android.R.color.holo_red_dark
                else -> android.R.color.darker_gray
            }

            textEstado.setTextColor(itemView.context.getColor(color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_prestamo, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Prestamo>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}