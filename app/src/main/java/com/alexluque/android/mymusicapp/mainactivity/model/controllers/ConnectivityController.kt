package com.alexluque.android.mymusicapp.mainactivity.model.controllers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.extensions.makeLongSnackbar

object ConnectivityController {

    private var view: View? = null
    private var context: Context? = null
    private var hasInternet: Boolean = true

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

    fun registerCallback(context: Context, view: View) {
        this.context = context
        this.view = view
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun runIfConnected(f: () -> Unit) {
        when (hasInternet) {
            true -> f()
            else -> context?.let {
                view?.makeLongSnackbar(context!!.getString(R.string.no_internet))
            }
        }
    }
}