package com.example.base.api.response

import com.example.base.entity.Currently
import com.example.base.entity.Daily
import com.example.base.entity.Hourly
import com.google.gson.annotations.SerializedName

data class WeatherResponse(

        @SerializedName("latitude") val latitude: String,

        @SerializedName("longitude") val longitude: String,

        @SerializedName("timezone") val timezone: String,

        @SerializedName("currently") val currently: Currently,

        @SerializedName("hourly") val hourly: Hourly,

        @SerializedName("daily") val daily: Daily,

        @SerializedName("offset") val offset: Int

)
