package com.example.loginapp.utils

import java.security.MessageDigest

object PasswordUtils {

    fun hashPassword(password: String): String {
        try {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error al hashear la contraseña", e)
        }
    }

    fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        val newHash = hashPassword(inputPassword)
        return newHash == storedHash
    }
}