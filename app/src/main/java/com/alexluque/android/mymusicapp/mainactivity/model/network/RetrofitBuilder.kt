package com.alexluque.android.mymusicapp.mainactivity.model.network

import com.alexluque.android.mymusicapp.mainactivity.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            ).connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    val deezerInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("https://api.deezer.com/")
    }

    val musicoveryInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("http://musicovery.com/api/V6/")
    }

    val geocodingInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("https://maps.googleapis.com/maps/api/geocode/")
    }

    private fun Retrofit.Builder.getInstance(url: String): Retrofit =
        this.baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
