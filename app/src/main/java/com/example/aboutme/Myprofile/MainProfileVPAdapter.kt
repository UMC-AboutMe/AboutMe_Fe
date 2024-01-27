package com.example.aboutme.Myprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.databinding.ItemMultiprofileBinding

class MainProfileVPAdapter : ListAdapter<MultiProfileData, MainProfileVPAdapter.MainItemViewHolder>(
    MainListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val binding = ItemMultiprofileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MainItemViewHolder(private val binding: ItemMultiprofileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MultiProfileData) {
            binding.multiProfileCharIv.setImageResource(item.profileImageResId)
            binding.multiProfileNameTv.text = item.name
            binding.multiProfileNumberTv.text = item.phoneNumber
        }
    }

    class MainListDiffCallback : DiffUtil.ItemCallback<MultiProfileData>() {
        override fun areItemsTheSame(oldItem: MultiProfileData, newItem: MultiProfileData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MultiProfileData, newItem: MultiProfileData): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
