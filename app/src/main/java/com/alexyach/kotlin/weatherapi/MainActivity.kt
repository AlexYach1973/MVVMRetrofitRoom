package com.alexyach.kotlin.weatherapi

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.MutableLiveData
import com.alexyach.kotlin.weatherapi.databinding.ActivityMainBinding
import com.alexyach.kotlin.weatherapi.di.SourcesModule
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByLocation
import com.alexyach.kotlin.weatherapi.room.IWeatherDAO
import com.alexyach.kotlin.weatherapi.ui.weatherlist.WeatherListFragment
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** TESTING */
//    @Inject lateinit var roomTest: IWeatherDAO

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.d("myLogs","MainActivity @Inject: $roomTest")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }

        /** Network */
//        val connectivityManager = // теж працює
//        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager
            .registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

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
        // Network
        private var onNetwork: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        fun getOnNetwork(): MutableLiveData<Boolean> = onNetwork
        fun setOnNetwork(b: Boolean) {
            // Викликається з фонового потоку: ConnectivityThread
//            Log.d("myLogs", "App Thread: ${Thread.currentThread().name}")
            onNetwork.postValue(b)
        }
    }
}

