package com.example.loginapp.model

import java.io.Serializable

data class Producto(
    val id: Int? = null,
    val nombre: String,
    val estado: String,
    val fecha_entrada: String,
    val fecha_salida: String,
    val cantidad: Int
) : Serializable
