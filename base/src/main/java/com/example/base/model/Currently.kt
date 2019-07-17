package com.example.base.model

import com.google.gson.annotations.SerializedName

data class Currently(

        @SerializedName("time") val time: Long,

        @SerializedName("summary") val summary: String,

        @SerializedName("icon") val icon: String,

        @SerializedName("precipIntensity") val precipIntensity: Double,

        @SerializedName("precipProbability") val precipProbability: Double,

        @SerializedName("temperature") val temperature: Double,

        @SerializedName("apparentTemperature") val apparentTemperature: Double,

        @SerializedName("dewPoint") val dewPoint: Double,

        @SerializedName("humidity") val humidity: Double,

        @SerializedName("pressure") val pressure: Double,

        @SerializedName("windSpeed") val windSpeed: Double,

        @SerializedName("windGust") val windGust: Double,

        @SerializedName("windBearing") val windBearing: Int,

        @SerializedName("cloudCover") val cloudCover: Double,

        @SerializedName("uvIndex") val uvIndex: Int,

        @SerializedName("visibility") val visibility: Double,

        @SerializedName("ozone") val ozone: Double

)
