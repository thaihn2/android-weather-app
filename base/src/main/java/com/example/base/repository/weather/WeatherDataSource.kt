package com.example.base.repository.weather

import android.location.Location
import androidx.lifecycle.LiveData
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

    fun getInfo(): LiveData<List<Info>>

    fun getDataHourly(): LiveData<List<DataHourly>>

    fun getDataDaily(): LiveData<List<DataDaily>>

    fun getCurrently(): LiveData<Currently>
}
