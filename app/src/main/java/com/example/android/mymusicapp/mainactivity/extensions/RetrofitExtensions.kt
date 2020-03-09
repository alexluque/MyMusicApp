package com.example.android.mymusicapp.mainactivity.extensions

import com.example.android.mymusicapp.mainactivity.network.builders.RetrofitBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Retrofit.Builder.getInstance(url: String): Retrofit =
    this.baseUrl(url)
        .client(RetrofitBuilder.client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()