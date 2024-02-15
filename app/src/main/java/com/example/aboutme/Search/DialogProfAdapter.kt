package com.example.aboutme.Search

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutme.R

class DialogProfAdapter(private val context: Context) :
    RecyclerView.Adapter<DialogProfAdapter.ViewHolder> () {

    var datas = mutableListOf<DialogProfData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profImg: ImageView = itemView.findViewById(R.id.select_profile_iv)
        private val profName: TextView = itemView.findViewById(R.id.select_profile_name_et)
        private val profName2: TextView = itemView.findViewById(R.id.select_checkBox)
        private val profNum: TextView = itemView.findViewById(R.id.select_profile_num_et)

        fun bind(item: DialogProfData) {
            Glide.with(itemView).load(item.profile_img).into(profImg)
            profName.text = item.profile_name
            profName2.text = item.profile_name
            profNum.text = item.profile_num
        }
    }
}