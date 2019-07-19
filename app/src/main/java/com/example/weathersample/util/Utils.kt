package com.example.weathersample.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun changeIconToName(@Icon.IconState name: String) = when (name) {
        Icon.CLEAR_DAY -> "ic_clear_day"
        Icon.CLEAR_NIGHT -> "ic_clear_night"
        Icon.RAIN -> "ic_rain"
        Icon.CLOUDY -> "ic_cloudy"
        Icon.FOG -> "ic_fog"
        Icon.PARTLY_CLOUDY_DAY -> "ic_partly_cloudy_day"
        Icon.PARTLY_CLOUDY_NIGHT -> "ic_partly_cloudy_night"
        Icon.SLEET -> "ic_sleet"
        Icon.SNOW -> "ic_snow"
        Icon.WIND -> "ic_wind"
        else -> "ic_clear_day"
    }

    fun getImage(imageName: String, context: Context): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    fun makeTemp() = 0x00B0.toChar()

    fun convertTime(format: String, time: Long): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(time * 1000)
    }

    fun convertTimeToHour(time: Long): String {
        val hour = convertTime("HH", time).toInt()
        val day = convertTime("dd", time).toInt()
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        return if (day != currentDay) {
            hour.toString() + "h"
        } else {
            if (hour == currentHour) "Now"
            else hour.toString() + "h"
        }
    }

    fun convertTimeToDay(time: Long): String {
        return SimpleDateFormat("EEEE", Locale.ENGLISH).format(time * 1000).toString()
    }

    fun changeTempFToC(tempF: Double): Int {
        val result = (tempF - 32) / 1.8
        return result.toInt()
    }
}
