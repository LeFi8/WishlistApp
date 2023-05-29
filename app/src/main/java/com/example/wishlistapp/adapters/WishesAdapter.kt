package com.example.wishlistapp.adapters

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wishlistapp.R
import com.example.wishlistapp.data.model.Wish
import com.example.wishlistapp.databinding.ListItemBinding

class WishItemViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wish: Wish) {
        binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
        binding.textView.text = wish.name
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

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: WishItemViewHolder, position: Int) {
        data.add(Wish(1, "TEST", 10.0, "PJATK"))
        holder.bind(data[position])
        val binding = holder.getBinding()
        refresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        handler.post {
            notifyDataSetChanged()
        }
    }


}