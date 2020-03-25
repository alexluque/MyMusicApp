package com.alexluque.android.mymusicapp.mainactivity.controller

import android.annotation.SuppressLint
import android.location.Location
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getCountry
import com.google.android.gms.location.FusedLocationProviderClient
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationRecommendationsListener(
    private val mapsKey: String,
    private val onRecommendClicked: (country: String) -> Unit,
    private val fusedClient: FusedLocationProviderClient) : PermissionListener {

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        fusedClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                ConnectivityController.runIfConnected {
                    showRecommendations(location)
                }
            }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) = onRecommendClicked(DEFAULT_COUNTRY)

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()

    private fun showRecommendations(location: Location?) {
        when (location) {
            null -> onRecommendClicked(DEFAULT_COUNTRY)
            else -> {
                ConnectivityController.runIfConnected {
                    GlobalScope.launch(Dispatchers.IO) {
                        val country = getCountry("${location.latitude},${location.longitude}", mapsKey)

                        when (country.isNullOrEmpty()) {
                            true -> onRecommendClicked(DEFAULT_COUNTRY)
                            else -> onRecommendClicked(country)
                        }
                    }
                }
            }
        }
    }

    private companion object {
        private const val DEFAULT_COUNTRY = "usa"
    }

}