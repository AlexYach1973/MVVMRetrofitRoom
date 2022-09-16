package com.alexyach.kotlin.weatherapi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.AccessController.getContext
import javax.inject.Inject

class NetworkAccess (appContext: Context) {
    val context = appContext

    @RequiresApi(Build.VERSION_CODES.N)
    fun initNetworkAccess() {
        getSystemService(context, ConnectivityManager::class.java)?.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    setOnNetwork(true)
                    Log.d("myLogs", "onAvailable: $network")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    setOnNetwork(false)
                    Log.d("myLogs", "onLost: $network")
                }
            })
    }


    companion object {
        private var onNetwork: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        fun getOnNetwork(): MutableLiveData<Boolean> = onNetwork

        fun setOnNetwork(isNetwork: Boolean) {
            onNetwork.postValue(isNetwork)

            // Викликається з фонового потоку: ConnectivityThread
//            Log.d("myLogs", "App Thread: ${Thread.currentThread().name}")
        }
    }
}