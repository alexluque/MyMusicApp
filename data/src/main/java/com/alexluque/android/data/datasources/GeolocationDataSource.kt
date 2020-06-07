package com.alexluque.android.data.datasources

import retrofit2.Retrofit

interface GeolocationDataSource {

    suspend fun getCountry(retrofit: Retrofit, latlng: String, mapsKey: String): String
}