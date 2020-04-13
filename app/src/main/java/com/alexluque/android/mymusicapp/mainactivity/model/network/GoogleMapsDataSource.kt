package com.alexluque.android.mymusicapp.mainactivity.model.network

import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MapsGeocodingService
import com.example.android.data.datasources.GeolocationDataSource

class GoogleMapsDataSource : GeolocationDataSource {

    override suspend fun getCountry(latlng: String, mapsKey: String): String =
        RetrofitBuilder.geocodingInstance
            .create(MapsGeocodingService::class.java)
            .getAddresses(latlng, mapsKey)
            .results
            .firstOrNull()
            ?.formatted_address
            ?.split(",")
            ?.lastOrNull()
            .orEmpty()
}