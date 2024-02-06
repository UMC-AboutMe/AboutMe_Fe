package com.example.aboutme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aboutme.databinding.ItemAgitBinding

class AgitSpaceRVAdapter(val items : MutableList<AgitSpaceData>) :RecyclerView.Adapter<AgitSpaceRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgitSpaceRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_agit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items : AgitSpaceData){
            val spaceImage = itemView.findViewById<ImageView>(R.id.space1_iv)
            val spaceName = itemView.findViewById<TextView>(R.id.spaceName_tv)

            spaceImage.setImageResource(items.space_img)
            spaceName.text = items.space_name
        }
    }
}

