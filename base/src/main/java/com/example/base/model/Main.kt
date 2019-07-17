package com.example.base.model

import com.google.gson.annotations.SerializedName

data class Main(

        @SerializedName("temp") val temp: Double,

        @SerializedName("temp_min") val temp_min: Double,

        @SerializedName("temp_max") val temp_max: Double,

        @SerializedName("pressure") val pressure: Double,

        @SerializedName("sea_level") val sea_level: Double,

        @SerializedName("grnd_level") val grnd_level: Double,

        @SerializedName("humidity") val humidity: Double,

        @SerializedName("temp_kf") val temp_kf: Double

)
