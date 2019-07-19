package com.example.weathersample.util

import androidx.annotation.StringDef

class Icon {

    @StringDef(CLEAR_DAY, CLEAR_NIGHT, RAIN, SNOW, CLOUDY, FOG, SLEET, WIND, PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT)
    annotation class IconState

    companion object {
        const val CLEAR_DAY = "clear-day"
        const val CLEAR_NIGHT = "clear-night"
        const val RAIN = "rain"
        const val SNOW = "snow"
        const val SLEET = "sleet"
        const val WIND = "wind"
        const val FOG = "fog"
        const val CLOUDY = "cloudy"
        const val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
        const val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"
    }
}
