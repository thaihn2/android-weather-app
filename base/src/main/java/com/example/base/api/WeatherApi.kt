package com.example.base.api

import com.example.base.api.response.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {

    @GET("forecast/{id}/{lat},{long}")
    fun getWeather(
            @Path(value = "id", encoded = true) key: String,
            @Path(value = "lat", encoded = true) lat: Double,
            @Path(value = "long", encoded = true) long: Double
    ): Observable<WeatherResponse>
}
