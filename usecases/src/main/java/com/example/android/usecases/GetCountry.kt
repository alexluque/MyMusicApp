package com.example.android.usecases

import com.example.android.data.repositories.GeolocationRepository
import retrofit2.Retrofit

class GetCountry(
    private val repository: GeolocationRepository
) {

    suspend fun invoke(retrofit: Retrofit, latlng:String, mapsKey: String): String = repository.getCountry(retrofit, latlng, mapsKey)
}