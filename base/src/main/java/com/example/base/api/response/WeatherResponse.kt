package com.example.base.api.response

import com.example.base.model.Currently
import com.example.base.model.Daily
import com.example.base.model.Hourly
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
