package com.example.base.repository.weather

import android.location.Location
import com.example.base.data.network.response.WeatherResponse
import io.reactivex.Observable

interface WeatherDataSource {

    fun getWeather(location: Location): Observable<WeatherResponse>
}
