package com.example.wishlistapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wishlistapp.databinding.ListItemBinding

class WishItemViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
        binding.textView.text = "HAHAHAHHA"
    }

    fun getBinding() = binding
}

class WishItemAdapter : RecyclerView.Adapter<WishItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return WishItemViewHolder(binding)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: WishItemViewHolder, position: Int) {
        val binding = holder.getBinding()
        holder.bind()
        refresh()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(){
        notifyDataSetChanged()
    }


}