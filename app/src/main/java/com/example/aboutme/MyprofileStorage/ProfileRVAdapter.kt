package com.example.aboutme.MyprofileStorage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.R

class ProfileRVAdapter(val items : MutableList<ProfileData>) :RecyclerView.Adapter<ProfileRVAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

    }

    //객체 저장 변수
    private var mOnItemClickListener: OnItemClickListener? = null

    //객체 전달 변수
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                Log.d("ProfileRVAdapter", "Item clicked at position $pos")
                if (pos != RecyclerView.NO_POSITION && mOnItemClickListener != null){
                    mOnItemClickListener!!.onItemClick(itemView, pos)
                }
            }
        }

        
    }

}

