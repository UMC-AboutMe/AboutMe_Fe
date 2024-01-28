package com.example.aboutme.Myprofile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.MyprofileStorage.ProfileStorageDetailActivity
import com.example.aboutme.databinding.ItemAddProfileBinding
import com.example.aboutme.databinding.ItemMultiprofileBinding
import com.kakao.sdk.template.model.Content
import kotlin.coroutines.coroutineContext

class MainProfileVPAdapter : ListAdapter<MultiProfileData, RecyclerView.ViewHolder>(
    MainListDiffCallback()
) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_ADD_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemMultiprofileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MainItemViewHolder(binding)
        } else {
            val binding = ItemAddProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            binding.profileAddBtn.setOnClickListener {
                Toast.makeText(parent.context, "프로필 추가", Toast.LENGTH_SHORT).show()

                val intent = Intent(parent.context, MainActivity2::class.java)
                parent.context.startActivity(intent)
            }
            MainAddItemViewHolder(binding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainItemViewHolder -> holder.bind(getItem(position) as MultiProfileData)
            is MainAddItemViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        // 마지막에 추가 항목을 위한 1을 더합니다.
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        // 마지막 항목인 경우 추가 항목을 위한 VIEW_TYPE_ADD_ITEM을 반환합니다.
        return if (position < super.getItemCount()) VIEW_TYPE_ITEM else VIEW_TYPE_ADD_ITEM
    }

    inner class MainItemViewHolder(private val binding: ItemMultiprofileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MultiProfileData) {
            binding.multiProfileCharIv.setImageResource(item.profileImageResId)
            binding.multiProfileNameTv.text = item.name
            binding.multiProfileNumberTv.text = item.phoneNumber
        }
    }

    inner class MainAddItemViewHolder(private val binding: ItemAddProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // 추가 항목에 대한 바인딩 로직을 추가하세요.
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
