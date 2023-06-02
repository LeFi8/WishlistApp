package com.example.wishlistapp.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wishlistapp.data.model.Wish
import com.example.wishlistapp.databinding.ListItemBinding
import com.example.wishlistapp.navigation.Navigable

class WishItemViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wish: Wish) {
        binding.photo.setImageURI(Uri.parse(wish.imageUri))
        binding.name.text = wish.name
        binding.price.text = wish.price.toString()
        binding.location.text = wish.location
    }

    fun getBinding() = binding
}

class WishItemAdapter : RecyclerView.Adapter<WishItemViewHolder>() {
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val data = mutableListOf<Wish>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return WishItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WishItemViewHolder, position: Int) {
        holder.bind(data[position])
        val binding = holder.getBinding()

        binding.root.setOnClickListener {
            (binding.root.context as? Navigable)?.navigate(
                Navigable.Destination.Edit,
                data[position].id
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replace(newData: List<Wish>) {
        data.clear()
        data.addAll(newData.reversed()) //order from last added

        handler.post {
            notifyDataSetChanged()
        }
    }
}