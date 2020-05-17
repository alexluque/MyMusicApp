package com.alexluque.android.mymusicapp.mainactivity.ui.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.makeLongSnackbar

/**
 * Class to control current Internet connectivity status.
 * Every activity should instantiate this [view]'s property with a valid view object in order to show the [Snackbar]
 * that would be shown to inform the user that no Internet connectivity is active.
 * [view] instantiation should be made within [onCreate] methods and also within [onResume] when the activity could be ever resumed.
 */
class ConnectivityController private constructor(){

    var view: View? = null
    var hasInternet: Boolean = false
    var networkRequest: NetworkRequest? = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        ?.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        ?.build()

    private var context: Context? = null
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
        networkRequest?.let { manager.registerNetworkCallback(it, networkCallback) }
    }

    fun runIfConnected(block: () -> Unit) {
        when (hasInternet) {
            true -> block()
            else -> context?.let {
                view?.makeLongSnackbar(context!!.getString(R.string.no_internet))
            }
        }
    }

    companion object {
        private var instance: ConnectivityController? = null

        fun getInstance(): ConnectivityController {
            if (instance == null)
                instance = ConnectivityController()
            return instance as ConnectivityController
        }
    }
}