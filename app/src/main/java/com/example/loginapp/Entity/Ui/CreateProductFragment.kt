package com.example.loginapp.entity.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.loginapp.R
import com.example.loginapp.entity.AppDatabase
import com.example.loginapp.entity.Product
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class CreateProductFragment : Fragment() {

    private lateinit var productNameEditText: TextInputEditText
    private lateinit var productDescriptionEditText: TextInputEditText
    private lateinit var productQuantityEditText: TextInputEditText
    private lateinit var saveProductButton: Button

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_product, container, false)

        db = AppDatabase.getDatabase(requireContext())

        productNameEditText = view.findViewById(R.id.productNameEditText)
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
        productQuantityEditText = view.findViewById(R.id.productQuantityEditText)
        saveProductButton = view.findViewById(R.id.saveProductButton)

        saveProductButton.setOnClickListener {
            saveNewProduct()
        }

        return view
    }

    private fun saveNewProduct() {
        val name = productNameEditText.text.toString().trim()
        val description = productDescriptionEditText.text.toString().trim()
        val quantityString = productQuantityEditText.text.toString().trim()

        if (name.isEmpty()) {
            productNameEditText.error = "El nombre es obligatorio"
            productNameEditText.requestFocus()
            return
        }

        val quantity = if (quantityString.isNotEmpty()) {
            try {
                quantityString.toInt()
            } catch (e: NumberFormatException) {
                productQuantityEditText.error = "Cantidad inválida"
                productQuantityEditText.requestFocus()
                return
            }
        } else {
            0
        }

        if (quantity < 0) {
            productQuantityEditText.error = "La cantidad no puede ser negativa"
            productQuantityEditText.requestFocus()
            return
        }

        val newProduct = Product(
            name = name,
            description = if (description.isEmpty()) null else description,
            quantity = quantity
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                db.productDao().insertProduct(newProduct)
                Toast.makeText(requireContext(), "Producto guardado exitosamente", Toast.LENGTH_SHORT).show()
                productNameEditText.text?.clear()
                productDescriptionEditText.text?.clear()
                productQuantityEditText.text?.clear()
                productNameEditText.requestFocus()
            } catch (e: Exception) {
                Log.e("CreateProductFragment", "Error al guardar producto", e)
                Toast.makeText(requireContext(), "Error al guardar el producto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}