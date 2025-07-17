package com.example.loginapp.api

import com.example.loginapp.LoginRequest
import com.example.loginapp.LoginResponse
import com.example.loginapp.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 🔐 Login
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // 📦 Productos
    @GET("productos")
    suspend fun getProductos(): List<Producto>

    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Unit>

    // 🗂️ Recursos
    @GET("recursos")
    suspend fun getRecursos(): List<Recurso>

    @POST("recursos")
    suspend fun createRecurso(@Body recurso: Recurso): Response<Recurso>

    @PUT("recursos/{id}")
    suspend fun actualizarRecurso(@Path("id") id: Int, @Body recurso: Recurso): Response<Recurso>

    @DELETE("recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: Int): Response<Unit>

    // 👥 Usuarios
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @POST("usuarios")
    suspend fun crearUsuario(@Body datos: Map<String, @JvmSuppressWildcards Any>): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body datos: Map<String, @JvmSuppressWildcards Any>): Response<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>

    // 📚 Préstamos
    @GET("prestamos")
    suspend fun getPrestamos(): List<Prestamo>

    @POST("prestamos")
    suspend fun crearPrestamo(@Body prestamo: Prestamo): Response<Prestamo>

    @PUT("prestamos/{id}")
    suspend fun actualizarPrestamo(@Path("id") id: Int, @Body prestamo: Prestamo): Response<Prestamo>

    @DELETE("prestamos/{id}")
    suspend fun eliminarPrestamo(@Path("id") id: Int): Response<Unit>

    // 🔐 Roles
    @GET("roles")
    suspend fun getRoles(): List<MakyRole>

    @POST("roles")
    suspend fun createRole(@Body role: MakyRole): Response<MakyRole>

    @PUT("roles/{id}")
    suspend fun actualizarRol(@Path("id") id: Int, @Body rol: MakyRole): Response<MakyRole>

    @DELETE("roles/{id}")
    suspend fun deleteRole(@Path("id") id: Int): Response<Unit>
}