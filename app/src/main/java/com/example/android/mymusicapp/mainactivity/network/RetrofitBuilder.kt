package com.example.android.mymusicapp.mainactivity.network

import com.example.android.mymusicapp.mainactivity.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private val client by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    val instance by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}