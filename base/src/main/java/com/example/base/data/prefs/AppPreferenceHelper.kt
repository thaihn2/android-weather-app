package com.example.base.data.prefs

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.base.injection.ApplicationContext
import com.example.base.injection.PreferenceInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceHelper @Inject constructor(
        @ApplicationContext private val context: Application
) {

    companion object {
        private const val PREF_KEY_CURRENT_COUNTRY = "PREF_KEY_CURRENT_COUNTRY"
    }

    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences("weatherPrefs", Context.MODE_PRIVATE)
    }

    fun setCounty(country: String) {
        prefs.edit().putString(PREF_KEY_CURRENT_COUNTRY, country).apply()
    }

    fun getCountry(): String? {
        return prefs.getString(PREF_KEY_CURRENT_COUNTRY, null)
    }
}
