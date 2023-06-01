package com.example.wishlistapp.data.model

data class Wish(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUri: String,
    val location: String
)
