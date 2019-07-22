package com.example.base.repository.weather

import android.location.Location
import com.example.base.data.local.WeatherLocalDataSource
import com.example.base.data.network.WeatherRemoteDataSource
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
        private val weatherRemoteDataSource: WeatherRemoteDataSource,
        private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherDataSource {

    override fun getWeather(location: Location): Observable<WeatherResponse> {
        return weatherRemoteDataSource.getWeather(location)
    }

    override fun getInfo(): Observable<List<Info>> {
        return weatherLocalDataSource.getInfo()
    }

    override fun getDataHourly(): Observable<List<DataHourly>> {
        return weatherLocalDataSource.getDataHourly()
    }

    override fun getDataDaily(): Observable<List<DataDaily>> {
        return weatherLocalDataSource.getDataDaily()
    }

    override fun getCurrently(): Observable<List<Currently>> {
        return weatherLocalDataSource.getCurrently()
    }

    override fun saveData(weatherResponse: WeatherResponse) {
        weatherLocalDataSource.saveData(weatherResponse)
    }

    override fun saveInfo(info: List<Info>) {
        weatherLocalDataSource.saveInfo(info)
    }
}
