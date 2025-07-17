package com.example.loginapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.databinding.ItemProductoBinding
import com.example.loginapp.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoAdapter(
    private val productos: MutableList<Producto>,
    private val onRefresh: () -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductoViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        val ctx = holder.itemView.context

        holder.binding.txtNombre.text = producto.nombre
        holder.binding.txtEstado.text = "Estado: ${producto.estado}"

        // ✏️ Clic para editar
        holder.binding.cardProducto.setOnClickListener {
            val intent = Intent(ctx, FormularioProductoActivity::class.java)
            intent.putExtra("producto", producto)
            ctx.startActivity(intent)
        }

        // 🗑️ Botón eliminar
        holder.binding.btnEliminarProducto.setOnClickListener {
            AlertDialog.Builder(ctx)
                .setTitle("Eliminar producto")
                .setMessage("¿Eliminar \"${producto.nombre}\"?")
                .setPositiveButton("Eliminar") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            RetrofitClient.instance.eliminarProducto(producto.id!!)
                            onRefresh()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun getItemCount(): Int = productos.size
}