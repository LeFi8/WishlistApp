package com.example.wishlistapp.fragments

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wishlistapp.databinding.FragmentWishEditBinding

class WishEditFragment(id: Long = -1) : Fragment() {

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
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addImage.setOnClickListener {
            createPicture()
        }

        binding.saveButton.setOnClickListener {
            val wishItemName = binding.name.text.toString()
            val wishItemPrice = binding.price.text.toString().toDouble()
            val wishItemDescription = binding.description.text.toString()
            val wishItemImageUri = imageUri
        }
    }

    private fun createPicture() {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        imageUri = requireContext().contentResolver.insert(uri, ct)
        cameraLauncher.launch(imageUri)
    }

}