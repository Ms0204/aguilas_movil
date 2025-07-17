package com.example.loginapp.model

data class Prestamo(
    val id: Int,
    val codigo: String,
    val usuario_id: Int,
    val recurso_id: Int,
    val fecha_prestamo: String,
    val fecha_devolucion: String,
    val estado: String,
    val usuario: Usuario? = null,
    val recurso: Recurso? = null
)