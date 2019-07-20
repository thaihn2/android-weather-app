package com.example.weathersample.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.data.network.response.WeatherResponse
import com.example.base.entity.Info
import com.example.base.extension.gone
import com.example.base.extension.nonNullSingle
import com.example.base.extension.observe
import com.example.base.extension.visible
import com.example.base.ui.BaseActivity
import com.example.weathersample.R
import com.example.weathersample.ui.main.adapter.HourlyAdapter
import com.example.weathersample.ui.main.adapter.InfoAdapter
import com.example.weathersample.ui.main.adapter.WeeklyAdapter
import com.example.weathersample.util.Utils
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel

    private val hourlyAdapter = HourlyAdapter()
    private val weeklyAdapter = WeeklyAdapter()
    private val infoAdapter = InfoAdapter()

    private var mLocationRequest = LocationRequest()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocation: Location? = null

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun initComponent(savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            requestLocationUpdates()
        }

        // Init location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    onNewLocation(it.lastLocation)
                }
            }
        }
        createLocationRequest()
        getLastLocation()

        recyclerHourly.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }

        recyclerWeekly.apply {
            adapter = weeklyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }

        recyclerInfo.apply {
            adapter = infoAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (mLocation != null) {
                swipeRefreshLayout.isRefreshing = true
                mLocation?.let {
                    mainViewModel.getWeather(it)
                }
            } else {
                Toast.makeText(this, "Unknow location", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        mainViewModel.getWeatherResponse()
                .nonNullSingle()
                .observe(this) {
                    progressbar.gone()
                    swipeRefreshLayout.isRefreshing = false
                    updateUi(it)
                }

        mainViewModel.getError()
                .nonNullSingle()
                .observe(this) {
                    swipeRefreshLayout.isRefreshing = false
                    progressbar.gone()
                    Toast.makeText(this, "${it.cause}", Toast.LENGTH_SHORT).show()
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    requestLocationUpdates()
                }
                else -> {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUi(weatherResponse: WeatherResponse) {
        hourlyAdapter.updateAll(weatherResponse.hourly.data)
        weeklyAdapter.updateAll(weatherResponse.daily.data)
        handleInfo(weatherResponse)

        textTemp.text = Utils.changeTempFToC(weatherResponse.currently.temperature).toString() + Utils.makeTemp()
        textStatus.text = makeStatus(weatherResponse)
        textTitle.isSelected = true
    }

    private fun makeStatus(response: WeatherResponse): String {
        val maxTemp = Utils.changeTempFToC(
                response.daily.data[0].temperatureHigh).toString() + Utils.makeTemp()
        val minTemp = Utils.changeTempFToC(
                response.daily.data[0].temperatureLow).toString() + Utils.makeTemp()
        return "Today: ${response.currently.summary}. The high will be $maxTemp. ${response.daily.data[0].summary} With a low of $minTemp."
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        var result = ""
        val maxResult = 10

        val listAddress = Geocoder(this).getFromLocation(latitude, longitude, maxResult)

        if (listAddress.isNotEmpty()) {
            listAddress[0].let {
                result = "${it.adminArea}"
            }
        }
        return result
    }

    fun onNewLocation(location: Location) {
        Log.d(TAG, "Location: $location")
        mLocation = location

        progressbar.visible()
        mLocation?.let {
            mainViewModel.getWeather(it)
            val address = getAddressFromLocation(it.latitude, it.longitude)
            textTitle.text = address
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    task.result?.let {
                        onNewLocation(it)
                    }
                } else {
                    requestLocationUpdates()
                    Log.w(TAG, "Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        try {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper())
        } catch (unlikely: SecurityException) {

            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            AlertDialog.Builder(this)
                    .setTitle("Need locaiton permission")
                    .setMessage("We need access your location permission")
                    .setPositiveButton("OK") { dialogInterface, i ->
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
                    }
                    .create()
                    .show()
        } else {
            Log.i(TAG, "Requesting permission")
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun handleInfo(weatherResponse: WeatherResponse) {
        val infoList = ArrayList<Info>()
        infoList.add(
                Info("SUNRISE", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunriseTime)))
        infoList.add(
                Info("SUNSET", Utils.convertTime("HH:mm", weatherResponse.daily.data[0].sunsetTime)))
        infoList.add(
                Info("CHANGE OF RAIN", "80%"))
        val humidity = (weatherResponse.currently.humidity * 100).toInt().toString() + "%"
        infoList.add(
                Info("HUMIDITY", humidity))
        infoList.add(Info("WIND", weatherResponse.currently.windSpeed.toInt().toString() + " kph"))
        infoList.add(Info("PRESSURE", weatherResponse.currently.pressure.toInt().toString() + " hPa"))
        infoList.add(Info("VISIBILITY", "%.1f".format(weatherResponse.currently.visibility) + " km"))
        infoList.add(Info("UV INDEX", weatherResponse.currently.uvIndex.toString()))

        infoAdapter.updateAll(infoList)
    }
}
