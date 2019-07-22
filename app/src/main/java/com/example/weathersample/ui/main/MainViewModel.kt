package com.example.weathersample.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
import com.example.base.repository.weather.WeatherRepository
import com.example.weathersample.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(
        private val weatherRepository: WeatherRepository
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private var mInfo = MutableLiveData<List<Info>>()
    private var mCurrently = MutableLiveData<Currently>()
    private var mHourly = MutableLiveData<List<DataHourly>>()
    private var mDaily = MutableLiveData<List<DataDaily>>()
    private var mStatus = MutableLiveData<Boolean>()

    private var error = MutableLiveData<Throwable>()

    @SuppressLint("CheckResult")
    fun getWeather(location: Location) {
        weatherRepository.getWeather(location)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "response: $it")
                    saveDatabase(it)
                    mStatus.value = true
                }, {
                    Log.d(TAG, "error: $it")
                    error.value = it
                })
    }

    @SuppressLint("CheckResult")
    fun getWeatherFromDB() {
        weatherRepository.getCurrently()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "Local currently: $it")
                    mCurrently.value = it[0]
                }, {
                    Log.d(TAG, "error: $it")
                    error.value = it
                })
        weatherRepository.getDataDaily()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "Get DataDaily from db: $it")
                    mDaily.value = it
                }, {
                    Log.d(TAG, "Get DataDaily from db: $it")
                })
        weatherRepository.getDataHourly()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "Get DataDaily from db: $it")
                    mHourly.value = it
                }, {
                    Log.d(TAG, "Get DataDaily from db: $it")
                })
        weatherRepository.getInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "Get DataDaily from db: $it")
                    mInfo.value = it
                }, {
                    Log.d(TAG, "Get DataDaily from db: $it")
                })
    }

    private fun saveDatabase(weatherResponse: WeatherResponse) {
        val info = handleInfo(weatherResponse)
        weatherRepository.saveInfo(info)
        weatherRepository.saveData(weatherResponse)
        mInfo.value = info

        mCurrently.value = weatherResponse.currently
        mDaily.value = weatherResponse.daily.data
        mHourly.value = weatherResponse.hourly.data
    }

    private fun handleInfo(weatherResponse: WeatherResponse): List<Info> {
        val infoList = ArrayList<Info>()
        infoList.add(Info(0, "SUNRISE", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunriseTime)))
        infoList.add(Info(1, "SUNSET", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunsetTime)))
        infoList.add(Info(2, "CHANGE OF RAIN", "80%"))
        infoList.add(Info(3, "HUMIDITY", (weatherResponse.currently.humidity * 100).toInt().toString() + "%"))
        infoList.add(Info(4, "WIND", weatherResponse.currently.windSpeed.toInt().toString() + " kph"))
        infoList.add(Info(5, "PRESSURE", weatherResponse.currently.pressure.toInt().toString() + " hPa"))
        infoList.add(Info(6, "VISIBILITY", "%.1f".format(weatherResponse.currently.visibility) + " km"))
        infoList.add(Info(7, "UV INDEX", weatherResponse.currently.uvIndex.toString()))
        return infoList
    }

    fun getStatus(): LiveData<Boolean> = mStatus

    fun getHourlyLiveData(): LiveData<List<DataHourly>> = mHourly

    fun getDailyLiveData(): LiveData<List<DataDaily>> = mDaily

    fun getInfoLiveData(): LiveData<List<Info>> = mInfo

    fun getCurrentlyLiveData(): LiveData<Currently> = mCurrently

    fun getError(): LiveData<Throwable> = error
}
