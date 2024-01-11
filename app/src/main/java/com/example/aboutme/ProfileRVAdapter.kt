package com.example.aboutme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aboutme.databinding.ItemProfilestorageBinding

class ProfileRVAdapter(val items : MutableList<ProfileData>) :RecyclerView.Adapter<ProfileRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_profilestorage, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items : ProfileData){
            val profileImage = itemView.findViewById<ImageView>(R.id.profile1_iv)
            val profileName = itemView.findViewById<TextView>(R.id.profileName_tv)

            profileImage.setImageResource(items.profile_img)
            profileName.text = items.profile_name
        }
    }

}

