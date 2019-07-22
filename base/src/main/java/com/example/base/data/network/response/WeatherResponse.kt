package com.example.base.data.network.response

import com.example.base.entity.Currently
import com.example.base.entity.Daily
import com.example.base.entity.Hourly
import com.google.gson.annotations.SerializedName

data class WeatherResponse(

        @SerializedName("currently") val currently: Currently,

        @SerializedName("hourly") val hourly: Hourly,

        @SerializedName("daily") val daily: Daily

)
