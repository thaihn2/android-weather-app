package com.example.weathersample.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base.api.BaseNetwork
import com.example.base.api.response.WeatherResponse
import com.example.base.repository.weather.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private var weatherRepository: WeatherRepository

    private val weatherResponseLiveData = MutableLiveData<WeatherResponse>()
    private val error = MutableLiveData<Throwable>()

    init {
        weatherRepository = WeatherRepository(BaseNetwork.providerWeatherApi())
    }

    @SuppressLint("CheckResult")
    fun getWeather(location: Location) {
        weatherRepository.getWeather(location.latitude, location.longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            Log.d(TAG, "response: $it")
                            weatherResponseLiveData.value = it
                        },
                        {
                            Log.d(TAG, "error: $it")
                            error.value = it
                        })
    }

    fun getWeatherResponse(): LiveData<WeatherResponse> = weatherResponseLiveData

    fun getError(): LiveData<Throwable> = error
}
