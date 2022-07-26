package com.alexyach.kotlin.weatherapi.ui.weatherlist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alexyach.kotlin.weatherapi.MainActivity.Companion.getOnNetwork
import com.alexyach.kotlin.weatherapi.R
import com.alexyach.kotlin.weatherapi.databinding.FragmentWeatherListBinding
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.ui.details.WeatherDetailsFragment

class WeatherListFragment : Fragment(), IOnItemClickAdapterWeatherList {

    private lateinit var viewModel: WeatherListViewModel
    private lateinit var adapter: WeatherListAdapter

    private var isNetwork: Boolean = false
    private var networkOrRoom: Boolean = true

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)

        // Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)

        // Спостереження
        observingLiveData()

        // FAB
        buttonFAB()

        // Кнопка пошуку по назві міста
        buttonSearchByNameCity()
    }

    /** Спостереження */
    private fun observingLiveData() {

        // Завантаження список міст
        viewModel.cityListLiveData()
            .observe(viewLifecycleOwner
            ) { dataList -> setAdapter(dataList) }

        // Пошук за назвою міста
        viewModel.getWeatherByNameCity()
            .observe(viewLifecycleOwner, object : Observer<WeatherModel> {
                override fun onChanged(weather: WeatherModel?) {

                    weather?.let {
                        if (weather.cityName == "-1") {
                            Toast.makeText(requireActivity(), " Немає такого міста",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            goToWeatherDetailsFragment(weather.cityName)
                        }
                    }
                }

            })

        // Стан мережі
        getOnNetwork().observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(network: Boolean) {
                viewModel.getCityList(network)
                showSearchByName(network)
                isNetwork = network
                networkOrRoom = !network
            }

        })
    }

    private fun buttonSearchByNameCity() {
        binding.weatherListButtonSearch.setOnClickListener {
            val nameCity = binding.weatherListEditText.text.toString()

            if (nameCity.isEmpty()) {
                Toast.makeText(requireActivity(), "Пустое поле", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.searchWeatherByNameCity(nameCity)
            // Скрыть клавиатуру
            hideKeyboardFrom(requireActivity(), binding.root)
        }
    }

    private fun buttonFAB() {
        binding.weatherListFab.setOnClickListener {

            if (isNetwork == false) {
                viewModel.getCityList(false)
                showSearchByName(false)
                Toast.makeText(requireActivity(), "Немає інтернету", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (networkOrRoom) {
                viewModel.getCityList(networkOrRoom)
            } else {
                viewModel.getCityList(networkOrRoom)
            }
            showSearchByName(networkOrRoom)
            networkOrRoom = !networkOrRoom

        }
    }

    private fun setAdapter(weatherList: List<WeatherModel>) {
        adapter = WeatherListAdapter(weatherList, this)
        binding.weatherListRecyclerView.adapter = adapter
    }

    // Обробка натискання на item
    override fun onItemClickAdapterWeatherList(weather: WeatherModel) {
        goToWeatherDetailsFragment(weather.cityName)
    }

    private fun goToWeatherDetailsFragment(nameCity: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .add(R.id.container, WeatherDetailsFragment.newInstance(nameCity, isNetwork))
            .addToBackStack("DetailsFragment")
            .commit()
    }

    private fun showSearchByName(b: Boolean) {
        if (b) {
            binding.weatherListEditText.visibility = View.VISIBLE
            binding.weatherListButtonSearch.visibility = View.VISIBLE
            binding.weatherListFab.setImageResource(R.drawable.ic_earth)
        } else {
            binding.weatherListEditText.visibility = View.GONE
            binding.weatherListButtonSearch.visibility = View.GONE
            binding.weatherListFab.setImageResource(R.drawable.ic_network_locked)
        }
    }

    /** Menu */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather_list_fragment, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_room -> {
                viewModel.deleteAllFromRoom()
                // Оновили
                if (networkOrRoom) {
                    viewModel.getCityList(!networkOrRoom)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Скрыть клавиатуру
    private fun hideKeyboardFrom(context: Context, view: View?) {
        val imm =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}