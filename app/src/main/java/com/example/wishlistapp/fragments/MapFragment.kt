package com.example.wishlistapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wishlistapp.databinding.FragmentMapBinding
import com.example.wishlistapp.mapServices.MapService


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapService: MapService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(inflater, container, false).also { fragmentBinding ->
            binding = fragmentBinding
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    mapService.locationOn()
                }
            }
            mapService = MapService(binding.map, requireActivity(), requestPermissionLauncher)
        }.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationButton.setOnClickListener {
            if (mapService.checkPermissions())
                mapService.locationOn()
            else mapService.requestPermissions()
        }

    }

}