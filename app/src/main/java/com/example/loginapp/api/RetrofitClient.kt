package com.example.loginapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.28:8000/api/" // localhost para el emulador

    // 🎯 Interceptor para ver peticiones/respuestas en Logcat
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("RetrofitDebug", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 🚦 Interceptor para forzar respuestas JSON (crítico para Laravel)
    private val headerInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json") // ✅ Laravel lo usa para enviar JSON
            .build()
        chain.proceed(request)
    }

    // 🧠 Cliente HTTP con ambos interceptores y timeout
    private val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    // 🎨 Gson flexible para evitar errores de parseo
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}