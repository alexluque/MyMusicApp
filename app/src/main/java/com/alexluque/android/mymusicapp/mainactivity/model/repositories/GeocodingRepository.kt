package com.alexluque.android.mymusicapp.mainactivity.model.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.services.MapsGeocodingService

suspend fun getCountry(latlng: String, mapsKey: String): String =
    RetrofitBuilder.geocodingInstance
        .create(MapsGeocodingService::class.java)
        .getAddresses(latlng, mapsKey)
        .results
        .firstOrNull()
        ?.formatted_address
        ?.split(",")
        ?.lastOrNull()
        .orEmpty()