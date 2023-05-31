package com.example.wishlistapp.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.wishlistapp.MapService.URIRequester
import com.example.wishlistapp.R
import com.example.wishlistapp.databinding.FragmentMapBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment(), MapEventsReceiver {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(inflater, container, false).also { fragmentBinding ->
            binding = fragmentBinding
            Configuration.getInstance().userAgentValue = requireActivity().packageName

            binding.map.apply {
                setTileSource(TileSourceFactory.MAPNIK)
            }

            if (checkPermissions()) {
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if (it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                        locationOn()
                    }
                }.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            } else locationOn()
        }.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationButton.setOnClickListener {
            locationOn()
        }

        binding.map.overlays.add(MapEventsOverlay(this))
    }

    private fun checkPermissions(): Boolean {
        val permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        return permissions.all {
            ContextCompat.checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED
        }
    }

    @SuppressLint("MissingPermission")
    private fun locationOn() {
        binding.map.apply {
            overlays.add(
                MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
            )

            requireActivity().getSystemService(LocationManager::class.java)
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    controller.animateTo(GeoPoint(it.latitude, it.longitude), 18.0, 1000)
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        GlobalScope.launch {
            val response = p?.let {
                URIRequester.requestURI(it.latitude, it.longitude)
                setPinPoint(p)
            }

            println(response)

        }
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }

    private fun setPinPoint(p: GeoPoint) {
        binding.map.apply {
            val marker = Marker(binding.map)
            marker.position = p
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.location_pin)
            binding.map.overlays.add(marker)
        }
    }

}