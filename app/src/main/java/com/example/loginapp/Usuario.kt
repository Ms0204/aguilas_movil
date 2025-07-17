package com.example.loginapp

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String?,
    val rol: String,
    val activo: Boolean,
    val roles: List<String>
)