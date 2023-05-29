package com.example.wishlistapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wishlistapp.data.entities.WishEntity

@Database(
    entities = [WishEntity::class],
    version = 1
)
abstract class WishDB : RoomDatabase() {
    abstract val wishes: WishDao

    companion object {
        fun open(context: Context): WishDB = Room.databaseBuilder(context, WishDB::class.java, "wishes.db").build()
    }
}