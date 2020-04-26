package com.alexluque.android.mymusicapp.mainactivity.controller.extensions

import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Retrofit.Builder.getInstance(url: String): Retrofit =
    this.baseUrl(url)
        .client(RetrofitBuilder.client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()