package com.example.wishlistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.wishlistapp.data.entities.WishEntity

@Dao
interface WishDao {
    @Query("SELECT * FROM wish;")
    fun getAll(): List<WishEntity>

    @Query("SELECT * from wish WHERE id = :id;")
    fun getBook(id: Long): WishEntity

    @Insert
    fun addBook(newWish: WishEntity)

    @Update
    fun updateBook(wish: WishEntity)

    @Delete
    fun removeBook(wishId: WishEntity)
}