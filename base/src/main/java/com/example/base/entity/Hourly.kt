package com.example.base.entity

import com.google.gson.annotations.SerializedName

data class Hourly(

        @SerializedName("data") val data: List<DataHourly>
)
