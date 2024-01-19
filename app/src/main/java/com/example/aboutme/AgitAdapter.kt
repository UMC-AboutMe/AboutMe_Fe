package com.example.aboutme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//class AgitAdapter : RecyclerView.Adapter<AgitAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.agit_item_layout, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindData(position)
//    }
//
//    override fun getItemCount(): Int {
//        return 8 // 데이터 아이템 개수
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val textView: TextView = itemView.findViewById(R.id.textView)
//
//        fun bindData(position: Int) {
//            textView.text = "Item $position"
//        }
//    }
//}
