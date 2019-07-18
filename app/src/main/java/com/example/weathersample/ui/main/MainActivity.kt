package com.example.weathersample.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.api.response.WeatherResponse
import com.example.base.entity.Info
import com.example.base.extension.nonNullSingle
import com.example.base.extension.observe
import com.example.base.ui.BaseActivity
import com.example.weathersample.R
import com.example.weathersample.ui.main.adapter.HourlyAdapter
import com.example.weathersample.ui.main.adapter.InfoAdapter
import com.example.weathersample.ui.main.adapter.WeeklyAdapter
import com.example.weathersample.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var mainViewModel: MainViewModel

    private val hourlyAdapter = HourlyAdapter()
    private val weeklyAdapter = WeeklyAdapter()
    private val infoAdapter = InfoAdapter()

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun initComponent(savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        recyclerHourly.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }

        recyclerWeekly.apply {
            adapter = weeklyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }

        recyclerInfo.apply {
            adapter = infoAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        mainViewModel.getWeather()


        mainViewModel.getWeatherResponse()
                .nonNullSingle()
                .observe(this) {
                    hourlyAdapter.updateAll(it.hourly.data)
                    weeklyAdapter.updateAll(it.daily.data)
                    handleInfo(it)
                }

        mainViewModel.getError()
                .nonNullSingle()
                .observe(this) {
                    Toast.makeText(this, "${it.cause}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun handleInfo(weatherResponse: WeatherResponse) {
        var infoList = ArrayList<Info>()
        infoList.add(
                Info("SUNRISE", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunriseTime)))
        infoList.add(
                Info("SUNSET", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunsetTime)))
        infoList.add(
                Info("CHANGE OF RAIN", "80%"))
        var humidity = (weatherResponse.currently.humidity * 100).toInt().toString() + "%"
        infoList.add(
                Info("HUMIDITY", humidity))
        infoList.add(Info("WIND", weatherResponse.currently.windSpeed.toInt().toString() + " kph"))
        infoList.add(Info("PRESSURE", weatherResponse.currently.pressure.toInt().toString() + " hPa"))
        infoList.add(Info("VISIBILITY", makeVisibility(weatherResponse.currently.visibility)))
        infoList.add(Info("UV INDEX", weatherResponse.currently.uvIndex.toString()))

        infoAdapter.updateAll(infoList)
    }

    private fun makeVisibility(visibility: Double): String {
        var _visi = "%.1f".format(visibility)
        return _visi + " km"
    }
}
