package com.alexluque.android.mymusicapp.mainactivity.model.controllers

import android.content.Context
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MapsGeocodingService
import com.alexluque.android.mymusicapp.mainactivity.presenters.RecommendationsActivityPresenter.Companion.DEFAULT_COUNTRY

fun getCountry(latlng: String, context: Context): String {
    val service = RetrofitBuilder.geocodingInstance.create(MapsGeocodingService::class.java)
    val response = service.getAddresses(latlng, context.getString(R.string.google_maps_key)).execute()
    val geocodingAddressSeparator = ","

    when (response.isSuccessful) {
        true -> {
            response.body()?.let {
                return it.results
                    .firstOrNull()
                    ?.formatted_address
                    ?.split(geocodingAddressSeparator)
                    ?.lastOrNull()
                    .orEmpty()
            }
            return String()
        }
        else -> return DEFAULT_COUNTRY
    }
}