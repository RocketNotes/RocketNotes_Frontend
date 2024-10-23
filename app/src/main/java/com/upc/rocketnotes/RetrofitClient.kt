package com.upc.rocketnotes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL =
        "http://10.0.2.2:8081/api/v1/" // Usar la IP de localhost para el emulador de Android

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Muestra el cuerpo de las solicitudes y respuestas
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private var retrofit: Retrofit? = null

    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Usa el cliente configurado con el interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val placeHolder: PlaceHolder by lazy {
        retrofitInstance.create(PlaceHolder::class.java)
    }
}