package com.example.base.repository.weather

import android.location.Location
import com.example.base.data.network.WeatherRemoteDataSource
import com.example.base.data.network.response.WeatherResponse
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
        private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherDataSource {

    override fun getWeather(location: Location): Observable<WeatherResponse> {
        return weatherRemoteDataSource.getWeather(location)
    }
}
