package com.example.aboutme.Agit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.RetrofitMyspaceAgit.AgitSpaceData
import com.example.aboutme.R

class AgitSpaceRVAdapter(val items : MutableList<AgitSpaceData>) :RecyclerView.Adapter<AgitSpaceRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        private val spaceImage: ImageView = itemView.findViewById(R.id.space_iv)
        private val spaceName: TextView = itemView.findViewById(R.id.spaceName_tv)
        private val bookmarkButton: ImageView = itemView.findViewById(R.id.agit_bookmark)

        fun bindItems(item: AgitSpaceData) {
            spaceImage.setImageResource(item.spaceImg)
            spaceName.text = item.spaceName

            bookmarkButton.setOnClickListener {
                // 북마크된 이미지와 북마크 해제된 이미지를 번갈아가면서 설정합니다.
                if (item.isBookmarked) {
                    bookmarkButton.setImageResource(R.drawable.agit_unbookmarked)
                } else {
                    bookmarkButton.setImageResource(R.drawable.agit_bookmarked)
                }
                // 데이터 모델에서 북마크 상태를 업데이트합니다.
                item.isBookmarked = !item.isBookmarked
            }
        }
    }
}

