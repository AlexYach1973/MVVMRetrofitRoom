package com.alexyach.kotlin.weatherapi

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alexyach.kotlin.weatherapi.databinding.ActivityMainBinding
import com.alexyach.kotlin.weatherapi.ui.weatherlist.WeatherListFragment
import com.alexyach.kotlin.weatherapi.utils.NetworkAccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }

        NetworkAccess(this).initNetworkAccess()

    }

}

