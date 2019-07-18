package com.example.base.repository.weather

import com.example.base.api.WeatherApi
import com.example.base.api.response.WeatherResponse
import io.reactivex.Observable

class WeatherRepository(
        val weatherApi: WeatherApi
) {

    fun getWeather(latitude: Double,
                   longitude: Double): Observable<WeatherResponse> = weatherApi.getWeather(
            lat = latitude, long = longitude, key = "832976143348bc9e6a5f1b05bae34ee8")
}
