package com.alexluque.android.mymusicapp.mainactivity.ui.listeners

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.getCountry
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.MainActivityContract
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
    private val context: Context,
    private val contract: MainActivityContract,
    private val fusedClient: FusedLocationProviderClient) : PermissionListener {

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        fusedClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                ConnectivityController.runIfConnected { showRecommendations(location) }
            }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) = contract.showDefaultRecommendations()

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()

    private fun showRecommendations(location: Location?) {
        when (location) {
            null -> contract.showDefaultRecommendations()
            else -> {
                GlobalScope.launch(Dispatchers.IO) {
                    val country = getCountry("${location.latitude},${location.longitude}", context)
                    when (country.isNullOrEmpty()) {
                        true -> contract.showDefaultRecommendations()
                        else -> contract.showRecommendations(country)
                    }
                }
            }
        }
    }

}