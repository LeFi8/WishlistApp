package com.example.wishlistapp.mapServices

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapService(
    private val mapView: MapView,
    private val activity: Activity,
    private val activityResultLauncher: ActivityResultLauncher<Array<String>>,
) {

    private val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    init {
        Configuration.getInstance().userAgentValue = activity.packageName

        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
        }

        if (checkPermissions())
            locationOn()
        else requestPermissions()
    }

    fun checkPermissions(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions() {
        activityResultLauncher.launch(
            permissions.toTypedArray()
        )
        if (checkPermissions()) locationOn()
    }

    @SuppressLint("MissingPermission")
    fun locationOn() {
        mapView.apply {
            overlays.add(
                MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
            )

            activity.getSystemService(LocationManager::class.java)
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    controller.animateTo(GeoPoint(it.latitude, it.longitude), 18.0, 1000)
                }
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): String {
        if (checkPermissions()) {
            activity.getSystemService(LocationManager::class.java)
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    return URIRequester.requestLocationName(it.latitude, it.longitude)
                }
        } else {
            requestPermissions()
        }
        return ""
    }

    @SuppressLint("MissingPermission")
    fun getLatitudeLongitude() : Pair<Double, Double> {
        if (checkPermissions()) {
            activity.getSystemService(LocationManager::class.java)
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    return Pair(it.latitude, it.longitude)
                }
        } else {
            requestPermissions()
        }
        return Pair(0.0, 0.0)
    }
}