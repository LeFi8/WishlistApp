package com.example.wishlistapp.data.model

import android.net.Uri

data class Wish(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUri: Uri,
    val localization: String
)
