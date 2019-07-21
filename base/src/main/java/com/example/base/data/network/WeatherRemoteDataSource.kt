package com.example.base.data.network

import android.location.Location
import androidx.lifecycle.LiveData
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Currently
import com.example.base.entity.DataDaily
import com.example.base.entity.DataHourly
import com.example.base.entity.Info
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

    override fun saveData(weatherResponse: WeatherResponse) {
    }

    override fun saveInfo(info: List<Info>) {
    }

    override fun getInfo(): LiveData<List<Info>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrently(): LiveData<Currently> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataDaily(): LiveData<List<DataDaily>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataHourly(): LiveData<List<DataHourly>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
