package com.example.wishlistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlistapp.adapters.WishItemAdapter
import com.example.wishlistapp.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {

    private lateinit var binding: FragmentWishlistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWishlistBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WishItemAdapter()
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }


    }

}