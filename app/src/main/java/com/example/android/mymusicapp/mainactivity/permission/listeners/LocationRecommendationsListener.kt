package com.example.android.mymusicapp.mainactivity.permission.listeners

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import com.example.android.mymusicapp.mainactivity.contracts.MainActivityContract
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class LocationRecommendationsListener(private val context: Context, private val contract: MainActivityContract) :
    PermissionListener {

    @SuppressLint("MissingPermission")
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (location != null) {
            val geoCoder = Geocoder(context)
            val countryCode = geoCoder.getFromLocation(location.latitude, location.longitude, 10)[0].countryCode
            val countryName = Locale(countryCode).getDisplayName(Locale.ENGLISH)

            if (countryName.isEmpty()) {
                contract.showRandomRecommendations()
            } else {
                // TODO: implement get artists by country
            }
        } else {
            contract.showRandomRecommendations()
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        contract.showRandomRecommendations()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
        token.continuePermissionRequest();
    }
}