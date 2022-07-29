package com.alexyach.kotlin.weatherapi.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.alexyach.kotlin.weatherapi.MainActivity.Companion.getOnNetwork
import com.alexyach.kotlin.weatherapi.R
import com.alexyach.kotlin.weatherapi.databinding.FragmentWeatherDetailsBinding
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.utils.KEY_PARAM_IS_NETWORK
import com.alexyach.kotlin.weatherapi.utils.KEY_PARAM_WEATHER
import com.alexyach.kotlin.weatherapi.utils.loadImageWeather
import kotlin.properties.Delegates

class WeatherDetailsFragment : Fragment() {

    private lateinit var viewModel: WeatherDetailsViewModel

    lateinit var currentWeather: WeatherModel
    var isNetwork by Delegates.notNull<Boolean>()

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding: FragmentWeatherDetailsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherDetailsViewModel::class.java)

        currentWeather = arguments?.run {
            getParcelable<WeatherModel>(KEY_PARAM_WEATHER)
        } ?: WeatherModel()

        isNetwork = arguments?.getBoolean(KEY_PARAM_IS_NETWORK) ?: false

        // Завантажуємо дані
        viewModel.getWeatherDetailsFromRepository(currentWeather, isNetwork)

        /** Спостереження */
        viewModel.getWeatherDetailsAppState().observe(viewLifecycleOwner) {
            getResponseAppState(it)
        }
        // мережа
        getOnNetwork().observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(network: Boolean) {
                isNetwork = network
                viewModel.getWeatherDetailsFromRepository(currentWeather, isNetwork)
            }

        })
    }

    private fun getResponseAppState(state: WeatherDetailsAppState) {
        when (state) {
            is WeatherDetailsAppState.SuccessGetDetailsWeather -> {
                renderData(state.weather)
                showResult()
            }
            is WeatherDetailsAppState.ErrorGetDetailsWeather -> {
                Toast.makeText(requireActivity(), state.errorString, Toast.LENGTH_LONG).show()
                showResult()
            }

            WeatherDetailsAppState.Loading -> {
                loadingResult()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(weather: WeatherModel) {
        with(binding) {
            detailsFragmentCityName.text = weather.cityName
            detailsFragmentTemperatureValue.text = "температура:" +
                    (weather.temperature.toInt() - 273).toString() + " C"
            detailsFragmentHumidity.text = "вологість: " +
                    weather.humidity + "%"
            detailsFragmentPressure.text = "тиск: " + weather.pressure.toString() + " hPa"
            detailsFragmentCityCoordinates.text = "lat: ${weather.lat} lon: ${weather.lon}"
            detailsFragmentDescriptionWeather.text = weather.description
            detailsFragmentDate.text = " дата: ${weather.date} "

            detailsFragmentImageWeather.load(loadImageWeather(weather.icon)) {
                placeholder(R.drawable.loadingfast)
                error(android.R.drawable.ic_menu_info_details)
            }

        }
    }

    private fun loadingResult() {
        binding.apply {
            detailsFragmentLoadingLayout.visibility = View.VISIBLE
            detailsFragmentConstrLayout.visibility = View.GONE
        }
    }

    private fun showResult() {
        binding.apply {
            detailsFragmentLoadingLayout.visibility = View.GONE
            detailsFragmentConstrLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(weather: WeatherModel, isNetwork: Boolean): WeatherDetailsFragment {
            val fragment = WeatherDetailsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(KEY_PARAM_WEATHER, weather)
                putBoolean(KEY_PARAM_IS_NETWORK, isNetwork)
            }
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}