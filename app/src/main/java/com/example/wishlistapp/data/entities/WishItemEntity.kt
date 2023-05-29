package com.example.wishlistapp.data.entities

import androidx.room.*

@Entity(tableName = "wish")
data class WishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val description: String,
    val localization: String
)
