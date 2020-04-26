package com.alexluque.android.mymusicapp.mainactivity.ui.main

import android.annotation.SuppressLint
import android.location.Location
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.google.android.gms.location.FusedLocationProviderClient
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class LocationRecommendationsListener(
    private val mapsKey: String,
    private val onRecommendClicked: (mapsKey: String, latitude: Double, longitude: Double) -> Unit,
    private val fusedClient: FusedLocationProviderClient
) : PermissionListener {

    private val emptyDouble = 0.0

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        fusedClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                ConnectivityController.runIfConnected {
                    showRecommendations(location)
                }
            }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) = onRecommendClicked(mapsKey, emptyDouble, emptyDouble)

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()

    private fun showRecommendations(location: Location?) {
        when (location) {
            null -> onRecommendClicked(mapsKey, emptyDouble, emptyDouble)
            else -> onRecommendClicked(mapsKey, location.latitude, location.longitude)
        }
    }
}