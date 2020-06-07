package com.alexluque.android.mymusicapp.mainactivity.model.network

import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MapsGeocodingService
import com.alexluque.android.data.datasources.GeolocationDataSource
import retrofit2.Retrofit
import java.lang.Exception

class GoogleMapsDataSource : GeolocationDataSource {

    override suspend fun getCountry(retrofit: Retrofit, latlng: String, mapsKey: String): String =
        try {
            retrofit
                .create(MapsGeocodingService::class.java)
                .getAddresses(latlng, mapsKey)
                .results
                .firstOrNull()
                ?.formatted_address
                ?.split(",")
                ?.lastOrNull()
                ?.trim()
                .orEmpty()
        } catch (e: Exception) {
            String()
        }
}