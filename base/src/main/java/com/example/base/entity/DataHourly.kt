package com.example.base.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "hourly")
data class DataHourly(

        @PrimaryKey(autoGenerate = true) val id: Int = 0,

        @SerializedName("time") val time: Long,

        @SerializedName("summary") val summary: String,

        @SerializedName("icon") val icon: String,

        @SerializedName("precipIntensity") val precipIntensity: Double,

        @SerializedName("precipProbability") val precipProbability: Double,

        @SerializedName("precipType") val precipType: String?,

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
