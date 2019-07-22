package com.example.weathersample.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
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
import com.example.base.entity.DataDaily
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
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mMainViewModel: MainViewModel

    private val mHourlyAdapter = HourlyAdapter()
    private val mWeeklyAdapter = WeeklyAdapter()
    private val mInfoAdapter = InfoAdapter()

    private var mLocationRequest = LocationRequest()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocation: Location? = null

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun initComponent(savedInstanceState: Bundle?) {
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            requestLocationUpdates()
        }

        if (!isNetworkAvailable()) {
            mMainViewModel.getWeatherFromDB()
        }

        // Init location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult?.lastLocation?.let {
                    onNewLocation(it)
                }
            }
        }

        createLocationRequest()
        getLastLocation()

        recyclerHourly.apply {
            adapter = mHourlyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }

        recyclerWeekly.apply {
            adapter = mWeeklyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }

        recyclerInfo.apply {
            adapter = mInfoAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (!isNetworkAvailable()) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this, "Network unavailable", Toast.LENGTH_SHORT).show()
                mMainViewModel.getWeatherFromDB()
            } else {
                if (mLocation != null) {
                    swipeRefreshLayout.isRefreshing = true
                    mLocation?.let {
                        mMainViewModel.getWeather(it)
                    }
                } else {
                    Toast.makeText(this, "Unknow location", Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                    mMainViewModel.getWeatherFromDB()
                }
            }
        }

        mMainViewModel.getStatus().nonNullSingle().observe(this) {
            if (it) {
                swipeRefreshLayout.isRefreshing = false
                progressbar.gone()
            }
        }

        mMainViewModel.getCurrentlyLiveData().nonNullSingle().observe(this) {
            textTemp.text = Utils.changeTempFToC(it.temperature).toString() + Utils.makeTemp()
            textTitle.isSelected = true
        }

        mMainViewModel.getHourlyLiveData().nonNullSingle().observe(this) {
            mHourlyAdapter.updateAll(it)
        }

        mMainViewModel.getInfoLiveData().nonNullSingle().observe(this) {
            mInfoAdapter.updateAll(it)
        }

        mMainViewModel.getDailyLiveData().nonNullSingle().observe(this) {
            mWeeklyAdapter.updateAll(it)
            textStatus.text = makeStatus(it[0])
        }

        mMainViewModel.getError().nonNullSingle().observe(this) {
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

    fun onNewLocation(location: Location) {
        Log.d(TAG, "Location: $location")
        mLocation = location

        if (isNetworkAvailable()) {
            progressbar.visible()
            mMainViewModel.getWeather(location)
            val address = getAddressFromLocation(location.latitude, location.longitude)
            textTitle.text = address
        } else {
            textTitle.gone()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun makeStatus(dataDaily: DataDaily): String {
        val maxTemp = Utils.changeTempFToC(dataDaily.temperatureHigh).toString() + Utils.makeTemp()
        val minTemp = Utils.changeTempFToC(dataDaily.temperatureLow).toString() + Utils.makeTemp()
        return "The high will be $maxTemp. ${dataDaily.summary} With a low of $minTemp."
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        var result = ""
        val maxResult = 10

        val listAddress = Geocoder(this).getFromLocation(latitude, longitude, maxResult)
        if (listAddress.isNotEmpty()) {
            listAddress[0].let {
                result = it.adminArea
            }
        }
        return result
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
}
