package com.example.aboutme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeFragmentAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> () {

    var datas = mutableListOf<HomeFragmentData>()
    var onItemClick: ((Int) -> Unit)? = null //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position) // 클릭된 아이템의 인덱스를 전달
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val homeImg: ImageView = itemView.findViewById(R.id.homeImg)
        private val homeName: TextView = itemView.findViewById(R.id.homeName)

        fun bind(item: HomeFragmentData) {
            Glide.with(itemView).load(item.Img).into(homeImg)
            homeName.text = item.Name
        }
    }
}

