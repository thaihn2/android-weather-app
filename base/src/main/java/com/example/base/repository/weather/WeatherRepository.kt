package com.example.base.repository.weather

import com.example.base.api.WeatherApi
import com.example.base.api.response.WeatherResponse
import io.reactivex.Observable
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        val weatherApi: WeatherApi
) {

    fun getWeather(latitude: Double,
                   longitude: Double): Observable<WeatherResponse> = weatherApi.getWeather(
            lat = latitude, long = longitude, key = "")
    // TODO: Insert api key
}
