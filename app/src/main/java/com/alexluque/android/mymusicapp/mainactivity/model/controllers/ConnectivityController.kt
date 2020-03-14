package com.alexluque.android.mymusicapp.mainactivity.model.controllers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlin.properties.Delegates

object ConnectivityController {

    var hasInternet: Boolean by Delegates.observable(true) { _, _, _ ->
        networkCallback
    }

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            hasInternet = false
        }

        override fun onAvailable(network: Network?) {
            hasInternet = true
        }
    }

    fun registerCallback(context: Context) {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(networkRequest, networkCallback)
    }
}