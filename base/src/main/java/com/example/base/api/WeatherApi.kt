package com.example.base.api

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface WeatherApi {

    @GET("data/2.5/forecast")
    fun getWeather(
            @Query(value = "lat", encoded = true) lat: Double,
            @Query(value = "lon", encoded = true) lon: Double,
            @Query(value = "appid", encoded = true) key: String
    ): Observable
}
