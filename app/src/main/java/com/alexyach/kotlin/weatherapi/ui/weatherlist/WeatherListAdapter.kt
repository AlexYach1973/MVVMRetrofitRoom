package com.alexyach.kotlin.weatherapi.ui.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexyach.kotlin.weatherapi.databinding.FragmentWeatherListItemBinding
import com.alexyach.kotlin.weatherapi.model.WeatherModel

class WeatherListAdapter(
    private val dataList: List<WeatherModel>,
    private val listener: IOnItemClickAdapterWeatherList
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = FragmentWeatherListItemBinding
            .inflate(LayoutInflater.from(parent.context))

        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // ViewHolder
    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentWeatherListItemBinding.bind(itemView)

        fun bind(weather: WeatherModel) {
            binding.itemCityName.text = weather.cityName

            // Прослуховувач для елемента
            itemView.setOnClickListener {
                listener.onItemClickAdapterWeatherList(weather)
            }

        }
    }
}