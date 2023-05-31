package com.example.wishlistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlistapp.adapters.WishItemAdapter
import com.example.wishlistapp.data.WishDB
import com.example.wishlistapp.data.model.Wish
import com.example.wishlistapp.databinding.FragmentWishlistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishlistFragment : Fragment() {

    private lateinit var binding: FragmentWishlistBinding
    private var adapter : WishItemAdapter ?= null
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

        adapter = WishItemAdapter()
        loadData()
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() = CoroutineScope(Dispatchers.Main).launch {
        val wishes = withContext(Dispatchers.IO) {
            WishDB.open(requireContext()).wishes.getAll().map {
                Wish(
                    it.id,
                    it.name,
                    it.price,
                    it.imageUri,
                    it.localization
                )
            }
        }
        adapter?.replace(wishes)
    }
}