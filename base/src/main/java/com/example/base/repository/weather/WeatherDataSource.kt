package com.example.base.repository.weather

import android.location.Location
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
import io.reactivex.Observable

interface WeatherDataSource {

    fun getWeather(location: Location): Observable<WeatherResponse>

    fun saveData(weatherResponse: WeatherResponse)

    fun saveInfo(info: List<Info>)

    fun getInfo(): Observable<List<Info>>

    fun getDataHourly(): Observable<List<DataHourly>>

    fun getDataDaily(): Observable<List<DataDaily>>

    fun getCurrently(): Observable<List<Currently>>
}
