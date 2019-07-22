package com.example.base.data.local

import android.location.Location
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
import com.example.base.repository.weather.WeatherDataSource
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
        private val localDatabase: LocalDatabase
) : WeatherDataSource {

    override fun getWeather(location: Location): Observable<WeatherResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveData(weatherResponse: WeatherResponse) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            localDatabase.weatherDao().clearCurrently()
            localDatabase.weatherDao().clearDaily()
            localDatabase.weatherDao().clearHourly()
            localDatabase.weatherDao().saveCurrently(weatherResponse.currently)
            localDatabase.weatherDao().saveDaily(weatherResponse.daily.data)
            localDatabase.weatherDao().saveHourly(weatherResponse.hourly.data)
        }
    }

    override fun saveInfo(info: List<Info>) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            localDatabase.weatherDao().clearInfo()
            localDatabase.weatherDao().saveInfo(info)
        }
    }


    override fun getInfo(): Observable<List<Info>> {
        return localDatabase.weatherDao().queryInfo()
    }

    override fun getCurrently(): Observable<List<Currently>> {
        return localDatabase.weatherDao().queryCurrently()
    }

    override fun getDataDaily(): Observable<List<DataDaily>> {
        return localDatabase.weatherDao().queryDaily()
    }

    override fun getDataHourly(): Observable<List<DataHourly>> {
        return localDatabase.weatherDao().queryHourly()
    }
}
