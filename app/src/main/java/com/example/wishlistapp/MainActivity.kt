package com.example.wishlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, WishlistFragment(), WishlistFragment::class.java.name)
            .commit()

    }
}