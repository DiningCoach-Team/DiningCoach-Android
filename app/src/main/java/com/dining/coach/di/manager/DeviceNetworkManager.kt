package com.dining.coach.di.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class DeviceNetworkManager(private val context: Context) : ConnectivityManager.NetworkCallback() {
    private var networkRequest: NetworkRequest = object : NetworkRequest.Builder() {}
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    private lateinit var listener: NetworkListener
    private lateinit var connectivityManager: ConnectivityManager

    fun register(callback: NetworkListener) {
        listener = callback

        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        checkNetworkStatus(null)
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        checkNetworkStatus(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        checkNetworkStatus(network)
    }

    private fun checkNetworkStatus(network: Network?) {
        if (network != null) {
            if (this::listener.isInitialized) {
                if (chkTransport(network)) listener.isOnline() else listener.isOffline()
            } else {
                listener.isOffline()
            }
        } else {
            val curNetwork = connectivityManager.activeNetwork

            if (curNetwork == null) listener.isOffline() else {
                if (chkTransport(curNetwork)) listener.isOnline() else listener.isOffline()
            }
        }
    }

    private fun chkTransport(network: Network): Boolean {
        connectivityManager.getNetworkCapabilities(network)?.run {
            val wifi = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val cellular = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val ethernet = hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            val vpn = hasTransport(NetworkCapabilities.TRANSPORT_VPN)

            return wifi || cellular || ethernet || vpn
        }

        return false
    }

    interface NetworkListener {
        fun isOnline()
        fun isOffline()
    }
}