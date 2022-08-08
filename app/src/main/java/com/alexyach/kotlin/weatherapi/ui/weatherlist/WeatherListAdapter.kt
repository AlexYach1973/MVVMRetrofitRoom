package com.alexyach.kotlin.weatherapi.ui.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexyach.kotlin.weatherapi.databinding.FragmentWeatherListItemBinding
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import kotlinx.android.synthetic.main.fragment_weather_list_item.view.*

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

        fun bind(weather: WeatherModel) {
            itemView.item_city_name.text = weather.cityName

            // Прослуховувач для елемента
            itemView.setOnClickListener {
                listener.onItemClickAdapterWeatherList(weather)
            }

        }
    }
}