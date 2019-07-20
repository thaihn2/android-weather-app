package com.example.base.data.network

import android.location.Location
import com.example.base.data.network.response.WeatherResponse
import com.example.base.repository.weather.WeatherDataSource
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRemoteDataSource @Inject constructor(
        private val weatherApi: WeatherApi
) : WeatherDataSource {

    override fun getWeather(location: Location): Observable<WeatherResponse> {
        return weatherApi.getWeather(key = "832976143348bc9e6a5f1b05bae34ee8",
                lat = location.latitude, long = location.longitude)
    }
}
