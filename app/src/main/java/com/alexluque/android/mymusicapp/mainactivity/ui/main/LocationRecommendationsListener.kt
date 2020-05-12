package com.alexluque.android.mymusicapp.mainactivity.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.recommendations.RecommendationsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class LocationRecommendationsListener(
    private val context: Context,
    private val fusedClient: FusedLocationProviderClient
) : PermissionListener {

    private val emptyDouble = "0.0"

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        fusedClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                ConnectivityController.runIfConnected {
                    showRecommendations(location)
                }
            }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) = showRecommendations()

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()

    private fun showRecommendations(location: Location? = null) {
        val values = mutableListOf<Pair<String, String>>()

        when (location) {
            null -> {
                values.add(LATITUDE to emptyDouble)
                values.add(LONGITUDE to emptyDouble)
            }
            else -> {
                values.add(LATITUDE to "${location.latitude}")
                values.add(LONGITUDE to "${location.longitude}")
            }
        }

        context.myStartActivity(RecommendationsActivity::class.java, values)
    }

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }
}