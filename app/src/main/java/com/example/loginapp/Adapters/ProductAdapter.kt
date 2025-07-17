package com.example.loginapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.entity.Product
import com.example.loginapp.R

class ProductAdapter(
    private val products: List<Product>,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.productName)
        val descriptionText: TextView = itemView.findViewById(R.id.productDescription)
        val quantityText: TextView = itemView.findViewById(R.id.productQuantity)
        val statusText: TextView = itemView.findViewById(R.id.productStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameText.text = product.name
        holder.descriptionText.text = product.description ?: "Sin descripción"
        holder.quantityText.text = "Cantidad: ${product.quantity}"
        holder.statusText.text = "Estado: ${product.status}"

        holder.statusText.setTextColor(
            if (product.status.lowercase() == "disponible") Color.parseColor("#388E3C")
            else Color.parseColor("#D32F2F")
        )

        // Menú contextual
        holder.itemView.setOnLongClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.menu_product_item, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        onEdit(product)
                        true
                    }
                    R.id.menu_delete -> {
                        onDelete(product)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
            true
        }
    }

    override fun getItemCount(): Int = products.size
}