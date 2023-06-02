package com.example.wishlistapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wishlistapp.R
import com.example.wishlistapp.data.WishDB
import com.example.wishlistapp.data.entities.WishEntity
import com.example.wishlistapp.databinding.FragmentWishEditBinding
import com.example.wishlistapp.mapServices.Geofencing
import com.example.wishlistapp.mapServices.MapService
import com.example.wishlistapp.mapServices.URIRequester
import com.example.wishlistapp.navigation.Navigable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishEditFragment(private val editId: Long = -1L) : Fragment() {

    private lateinit var binding: FragmentWishEditBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private val onTakePhoto: (Boolean) -> Unit = { photo ->
        if (!photo) {
            imageUri?.let {
                requireContext().contentResolver
                    .delete(it, null, null)
            }
        } else {
            binding.image.setImageURI(imageUri)
            println(imageUri)
        }
    }
    private lateinit var mapService: MapService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (editId == -1L) return

        CoroutineScope(Dispatchers.Main).launch {
            val data = withContext(Dispatchers.IO) {
                WishDB.open(requireContext()).wishes.getWish(editId)
            }
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.isClickable = true
            binding.fragmentTitle.setText(R.string.edit)
            binding.name.setText(data.name)
            binding.price.setText(data.price.toString())
            binding.image.setImageURI(Uri.parse(data.imageUri))
            binding.description.setText(data.description)
            binding.location.setText(data.location)

            imageUri = Uri.parse(data.imageUri)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
        return FragmentWishEditBinding.inflate(inflater, container, false).also {
            binding = it
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    mapService.locationOn()
                }
            }
            mapService = MapService(it.map, requireActivity(), requestPermissionLauncher)
            it.map.setOnTouchListener { _, _ ->
                //disables user interaction with a map
                true
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addImage.setOnClickListener {
            createPicture()
        }

        binding.saveButton.setOnClickListener {
            if (!validateInput()) return@setOnClickListener

            if (editId == -1L) addToDb()
            else updateDb()

            val context = requireContext()
            CoroutineScope(Dispatchers.IO).launch {
                val latitude = mapService.getCurrentLatitude(binding.location.text.toString())
                val longitude = mapService.getCurrentLongitude(binding.location.text.toString())
                if (latitude != null && longitude != null) {
                    Geofencing.createGeofence(
                        context,
                        latitude,
                        longitude,
                        binding.name.text.toString()
                    )
                }
            }

            parentFragmentManager.popBackStack()
            (activity as? Navigable)?.navigate(Navigable.Destination.List)
            Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show()
        }

        binding.deleteButton.setOnClickListener {
            val context = binding.root.context
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setTitle(
                binding.root.resources.getString(
                    R.string.removing_are_you_sure,
                    binding.name.text.toString()
                )
            )
            alertDialog.setPositiveButton(binding.root.resources.getString(R.string.yes)) { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    val selectedWish =
                        WishDB.open(context).wishes.getWish(editId)
                    WishDB.open(context).wishes.removeWish(selectedWish)
                }
                parentFragmentManager.popBackStack()
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
                Toast.makeText(
                    context,
                    binding.root.resources.getString(R.string.removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertDialog.setNegativeButton(binding.root.resources.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        }

        binding.searchButton.setOnClickListener {
            if (mapService.checkPermissions()) {
                mapService.locationOn()
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        mapService.animateToLocation(binding.location.text.toString())
                    }
                }
            } else {
                mapService.requestPermissions()
            }
        }

        binding.locationButton.setOnClickListener {
            if (mapService.checkPermissions()) {
                mapService.locationOn()
                CoroutineScope(Dispatchers.Main).launch {
                    val currentLocation = withContext(Dispatchers.IO) {
                        mapService.getCurrentLocation()
                    }
                    binding.location.setText(currentLocation)
                }
            } else {
                mapService.requestPermissions()
            }
        }


    }

    private fun createPicture() {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "wish_item.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        imageUri = requireContext().contentResolver.insert(uri, ct)
        cameraLauncher.launch(imageUri)
    }

    private fun validateInput(): Boolean {
        var flag = true
        if (binding.name.text.isEmpty()) {
            binding.name.error = "This field cannot be empty"
            flag = false
        }
        if (binding.price.text.isEmpty()) {
            binding.price.error = "This field cannot be empty"
            flag = false
        }
        return flag
    }

    private fun addToDb() {
        val wishItemName = binding.name.text.toString()
        val wishItemPrice = binding.price.text.toString().toDouble()
        val wishItemDescription = binding.description.text.toString()
        val wishItemImageUri = imageUri.toString()
        val location = binding.location.text.toString()

        val newWish = WishEntity(
            name = wishItemName,
            price = wishItemPrice,
            description = wishItemDescription,
            imageUri = wishItemImageUri,
            location = location,
        )

        CoroutineScope(Dispatchers.IO).launch {
            WishDB.open(requireContext()).wishes.addWish(newWish)
        }
    }

    private fun updateDb() {
        val wishItemName = binding.name.text.toString()
        val wishItemPrice = binding.price.text.toString().toDouble()
        val wishItemDescription = binding.description.text.toString()
        val wishItemImageUri = imageUri.toString()
        val location = binding.location.text.toString()

        val wish = WishEntity(
            id = editId,
            name = wishItemName,
            price = wishItemPrice,
            description = wishItemDescription,
            imageUri = wishItemImageUri,
            location = location,
        )

        CoroutineScope(Dispatchers.IO).launch {
            WishDB.open(requireContext()).wishes.updateWish(wish)
        }
    }

}