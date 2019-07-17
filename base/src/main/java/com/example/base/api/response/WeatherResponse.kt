package com.example.base.api.response

import com.example.base.model.City
import com.google.gson.annotations.SerializedName

data class WeatherResponse(

        @SerializedName("city") val city: City,

        
)
