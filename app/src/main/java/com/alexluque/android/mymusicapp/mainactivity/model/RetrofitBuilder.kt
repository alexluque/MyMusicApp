package com.alexluque.android.mymusicapp.mainactivity.model

import com.alexluque.android.mymusicapp.mainactivity.BuildConfig
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.getInstance
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

    val geocodingInstance: Retrofit by lazy {
        Retrofit.Builder().getInstance("https://maps.googleapis.com/maps/api/geocode/")
    }
}