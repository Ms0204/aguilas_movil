package com.example.loginapp.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val activo: Boolean,
    val rol: String,
    val password: String? // si lo manejas en frontend
)