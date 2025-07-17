package com.example.loginapp.entity.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.entity.AppDatabase
import com.example.loginapp.entity.Product
import com.example.loginapp.R
import com.example.loginapp.adapters.ProductAdapter
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db = AppDatabase.getDatabase(requireContext())

        loadProducts()
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            val products = db.productDao().getAllProducts()
            recyclerView.adapter = ProductAdapter(
                products,
                onEdit = { product -> showEditDialog(product) },
                onDelete = { product -> deleteProduct(product) }
            )
        }
    }

    private fun deleteProduct(product: Product) {
        lifecycleScope.launch {
            db.productDao().deleteProduct(product)
            Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
            loadProducts()
        }
    }

    private fun showEditDialog(product: Product) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_product, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.editProductName)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.editProductDescription)
        val quantityEditText = dialogView.findViewById<EditText>(R.id.editProductQuantity)
        val statusSpinner = dialogView.findViewById<Spinner>(R.id.editProductStatus)

        // Rellenar campos con datos actuales
        nameEditText.setText(product.name)
        descriptionEditText.setText(product.description ?: "")
        quantityEditText.setText(product.quantity.toString())

        val statusOptions = listOf("disponible", "agotado")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter
        statusSpinner.setSelection(statusOptions.indexOf(product.status))

        AlertDialog.Builder(requireContext())
            .setTitle("Editar Producto")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = nameEditText.text.toString().trim()
                val newDescription = descriptionEditText.text.toString().trim()
                val newQuantity = quantityEditText.text.toString().toIntOrNull() ?: 0
                val newStatus = statusSpinner.selectedItem.toString()

                val updatedProduct = product.copy(
                    name = newName,
                    description = newDescription,
                    quantity = newQuantity,
                    status = newStatus
                )

                lifecycleScope.launch {
                    db.productDao().updateProduct(updatedProduct)
                    Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show()
                    loadProducts()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

