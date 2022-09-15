package com.alexyach.kotlin.weatherapi.ui.weatherlist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alexyach.kotlin.weatherapi.MainActivity.Companion.getOnNetwork
import com.alexyach.kotlin.weatherapi.R
import com.alexyach.kotlin.weatherapi.databinding.FragmentWeatherListBinding
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.ui.base.BaseFragment
import com.alexyach.kotlin.weatherapi.ui.details.WeatherDetailsFragment
import com.alexyach.kotlin.weatherapi.utils.KEY_GET_WEATHER_BY
import com.alexyach.kotlin.weatherapi.utils.NOT_FOUND_CITY
import com.alexyach.kotlin.weatherapi.utils.REQUEST_CODE_LOCATION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherListFragment : BaseFragment<FragmentWeatherListBinding,
        WeatherListViewModel>(), IOnItemClickAdapterWeatherList {

    override val viewModel: WeatherListViewModel by lazy {
        ViewModelProvider(this)[WeatherListViewModel::class.java]
    }

    /** TEST */
//    @Inject lateinit var testInject : IRepositoryByCityName

        override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWeatherListBinding.inflate(inflater, container, false)

    private lateinit var adapter: WeatherListAdapter
    private lateinit var locationManager: LocationManager

    private var isNetwork: Boolean = false
    private var networkOrRoom: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Спостереження
        observingLiveData()

        // FAB choice City
        buttonFABChoiceCityList()

        // FAB Location
        buttonFabLocation()

        // Кнопка пошуку по назві міста
        buttonSearchByNameCity()

        /** TEST */
//         Log.d("myLogs", "WeatherListFragment Inject: $testInject")
    }

    /** Спостереження */
    private fun observingLiveData() {

        // Завантаження список міст
        viewModel.cityListLiveData()
            .observe(viewLifecycleOwner) { dataList ->
                setAdapter(dataList)
            }

        // Пошук за назвою міста
        viewModel.getWeatherByNameCity()
            .observe(viewLifecycleOwner, object : Observer<WeatherModel> {
                override fun onChanged(weather: WeatherModel?) {

                    weather?.let {
                        if (weather.cityName == NOT_FOUND_CITY) {
                            toast(R.string.not_found_city)
                        } else {
                            goToWeatherDetailsFragment(weather)
                        }
                    }
                }
            })

        // Стан мережі
        getOnNetwork().observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(network: Boolean) {
                viewModel.getCityList(network)
                showViewWhenIsNetwork(network)
                isNetwork = network
                networkOrRoom = !network
            }

        })
    }

    private fun buttonSearchByNameCity() {
        binding.weatherListButtonSearch.setOnClickListener {
            val nameCity = binding.weatherListEditText.text.toString()

            if (nameCity.isEmpty()) {
                toast(R.string.field_empty)
                return@setOnClickListener
            }
            viewModel.searchWeatherByNameCity(nameCity)
            // Скрыть клавиатуру
            hideKeyboardFrom(requireActivity(), binding.root)
        }
    }

    private fun buttonFABChoiceCityList() {
        binding.weatherListFab.setOnClickListener {

            if (!isNetwork) {
                viewModel.getCityList(false)
                showViewWhenIsNetwork(false)
                toast(R.string.no_internet)
                return@setOnClickListener
            }
            viewModel.getCityList(networkOrRoom)
            showViewWhenIsNetwork(networkOrRoom)
            networkOrRoom = !networkOrRoom

        }
    }

    private fun buttonFabLocation() {
        binding.weatherListFragmentFABLocation.setOnClickListener {
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /** Отримати локацію по поточним координатам */
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Якщо працює GPS
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    Float.MAX_VALUE, // Це- милиця, щоб не оновлював постйно  дані
                    locationListener
                )
            } else {
                toast(R.string.no_gps)
            }
        } else {
            Log.d("myLogs", "getLocation - немає дозволу")
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            goToWeatherDetailsFragment(
                WeatherModel(
                    cityName = KEY_GET_WEATHER_BY,
                    lat = location.latitude,
                    lon = location.longitude
                )
            )
//            Log.d("myLogs", "locationListener: ${location.latitude}/${location.longitude}")
        }
    }

    /** Permission  */
    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
//        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    private fun checkPermission(permission: String) {
        val permResult = ContextCompat.checkSelfPermission(requireContext(), permission)

        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getLocation()

            // Якщо одного разу вже відмовили в дозволу...
        } else if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_massage) // Докладне роз'яснення
                .setPositiveButton(R.string.set_positive_button) { _, _ ->
                    permissionRequest(permission)
                }
                .setNegativeButton(R.string.set_negative_button) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(permission)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                    Log.d("myLogs", "onRequestPermissionsResult: є дозвіл ")
                } else {
                    Log.d("myLogs", "onRequestPermissionsResult: немає дозволу ")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /** ************** */

    private fun setAdapter(weatherList: List<WeatherModel>) {
        adapter = WeatherListAdapter(weatherList, this)
        binding.weatherListRecyclerView.adapter = adapter
    }

    // Обробка натискання на item
    override fun onItemClickAdapterWeatherList(weather: WeatherModel) {
        goToWeatherDetailsFragment(weather)
    }

    private fun goToWeatherDetailsFragment(weather: WeatherModel) {
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .add(R.id.container, WeatherDetailsFragment.newInstance(weather, isNetwork))
            .setReorderingAllowed(true) // щось там оптимізує при переході
            .addToBackStack("DetailsFragment")
            .commit()
    }

    private fun showViewWhenIsNetwork(networkOrRoom: Boolean) {
        if (networkOrRoom) {
            with(binding) {
                weatherListEditText.visibility = View.VISIBLE
                weatherListButtonSearch.visibility = View.VISIBLE
                weatherListFab.setImageResource(R.drawable.ic_earth)
                weatherListFragmentFABLocation.visibility = View.VISIBLE
            }

        } else {
            with(binding) {
                weatherListEditText.visibility = View.GONE
                weatherListButtonSearch.visibility = View.GONE
                weatherListFab.setImageResource(R.drawable.ic_network_locked)
                weatherListFragmentFABLocation.visibility = View.GONE
            }
        }
    }

    /** Menu */
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_weather_list_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_room -> {
                        actionDeleteRoom()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun actionDeleteRoom() {
        viewModel.deleteAllFromRoom()
//        adapter.notifyDataSetChanged()
        // Оновили
        if (isNetwork) {
            viewModel.getCityList(isNetwork)
        }
//        Log.d("myLogs", "WeatherListFragment actionDelete: $networkOrRoom")
    }

    // Сховати клавіатуру
    private fun hideKeyboardFrom(context: Context, view: View?) {
        val imm =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        fun newInstance() = WeatherListFragment()
    }
}