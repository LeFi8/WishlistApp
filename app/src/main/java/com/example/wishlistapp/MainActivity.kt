package com.example.wishlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wishlistapp.fragments.WishEditFragment
import com.example.wishlistapp.fragments.WishlistFragment
import com.example.wishlistapp.navigation.Navigable

class MainActivity : AppCompatActivity(), Navigable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, WishlistFragment(), WishlistFragment::class.java.name)
            .commit()

    }

    override fun navigate(to: Navigable.Destination, id: Long) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.container,
                    WishlistFragment(),
                    WishlistFragment::class.java.name
                )

                Navigable.Destination.Add -> {
                    replace(R.id.container, WishEditFragment(), WishEditFragment::class.java.name)
                    addToBackStack(WishEditFragment::class.java.name)
                }

                Navigable.Destination.Edit -> {
                    replace(R.id.container, WishEditFragment(id), WishEditFragment::class.java.name)
                    addToBackStack(WishEditFragment::class.java.name)
                }
            }.commit()
        }
    }


}