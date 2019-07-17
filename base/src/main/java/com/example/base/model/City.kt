package com.example.base.model

import com.google.gson.annotations.SerializedName

data class City(

        @SerializedName("id") val id: Int,

        @SerializedName("name") val name: String,

        @SerializedName("country") val country: String,

        @SerializedName("population") val population: Int,

        @SerializedName("timezone") val timezone: Int

)
