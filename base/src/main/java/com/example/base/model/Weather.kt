package com.example.base.model

import com.google.gson.annotations.SerializedName

data class Weather(

        @SerializedName("dt") val dt: Int,

        @SerializedName("dt_txt") val dt_txt: String,

        @SerializedName("main") val main: Main,

        @SerializedName("wind") val wind: Wind,

        @SerializedName("clouds") val cloud: Cloud

)
