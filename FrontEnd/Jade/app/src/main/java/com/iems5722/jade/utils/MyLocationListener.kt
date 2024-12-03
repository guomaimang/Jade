package com.iems5722.jade.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class MyLocationListener(context: Context) : LocationListener {
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    init {
        // Request location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        } catch (ex: SecurityException) {
            // Handle exception
        }
    }

    fun updateLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        } catch (ex: SecurityException) {
            // Handle exception
        }
        Log.e("UPDATE", "UPDATE LOCATION")
    }

    override fun onLocationChanged(location: Location) {
        // Called when a new location is found by the network location provider.
        if (location.latitude != 0.0 || location.longitude != 0.0) {
            latitude = location.latitude
            longitude = location.longitude
            // Do something with the location data
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Called when the provider status changes
    }

    override fun onProviderEnabled(provider: String) {
        // Called when the provider is enabled by the user
    }

    override fun onProviderDisabled(provider: String) {
        // Called when the provider is disabled by the user
    }

    fun getLocationString(): String {
        return "($latitude,$longitude)"
    }
}