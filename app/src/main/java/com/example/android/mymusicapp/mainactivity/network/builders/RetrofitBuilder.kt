package com.example.android.mymusicapp.mainactivity.network.builders

import com.example.android.mymusicapp.mainactivity.BuildConfig
import com.example.android.mymusicapp.mainactivity.extensions.getInstance
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitBuilder {

    val client: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).build()
    }

    val deezerInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("https://api.deezer.com/")
    }

    val musicoveryInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("http://musicovery.com/api/V6/")
    }
}