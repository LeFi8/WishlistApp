package com.example.wishlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.wishlistapp.databinding.ActivityMainBinding
import com.example.wishlistapp.fragments.MapFragment
import com.example.wishlistapp.fragments.WishEditFragment
import com.example.wishlistapp.fragments.WishlistFragment
import com.example.wishlistapp.navigation.Navigable

class MainActivity : AppCompatActivity(), Navigable, FragmentManager.OnBackStackChangedListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.hide()

        supportFragmentManager.addOnBackStackChangedListener(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, WishlistFragment(), WishlistFragment::class.java.name)
            .commit()

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_list -> {
                    navigate(Navigable.Destination.List)
                    true
                }
                R.id.menu_map -> {
                    navigate(Navigable.Destination.Map)
                    true
                }
                R.id.add_wish -> {
                    navigate(Navigable.Destination.Add)
                    true
                }
                else -> false
            }
        }

    }

    override fun navigate(to: Navigable.Destination, id: Long) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.fragmentContainerView,
                    WishlistFragment(),
                    WishlistFragment::class.java.name
                )

                Navigable.Destination.Map ->
                    replace(R.id.fragmentContainerView, MapFragment(), MapFragment::class.java.name)

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

    override fun onBackStackChanged() {
        if (supportFragmentManager.findFragmentById(binding.container.id) is WishEditFragment)
            binding.bottomNav.visibility = View.GONE
        else binding.bottomNav.visibility = View.VISIBLE

        if (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) is WishlistFragment)
            binding.bottomNav.menu.getItem(0).isChecked = true

        if (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) is MapFragment)
            binding.bottomNav.menu.getItem(1).isChecked = true
    }
}