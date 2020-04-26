package com.example.android.usecases

import com.example.android.data.repositories.GeolocationRepository

class GetCountry(
    private val repository: GeolocationRepository
) {

    suspend fun invoke(latlng:String, mapsKey: String): String = repository.getCountry(latlng, mapsKey)
}