package com.example.aboutme.Agit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.RetrofitMyspaceAgit.AgitSpaceData
import com.example.aboutme.R

class AgitSpaceRVAdapter(val items: MutableList<AgitSpaceData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_agit, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindItems(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentLayout: ConstraintLayout = itemView.findViewById(R.id.item_agit)
        private val spaceImage: ImageView = itemView.findViewById(R.id.space_iv)
        private val spaceName: TextView = itemView.findViewById(R.id.spaceName_tv)
        private val bookmarkButton: ImageView = itemView.findViewById(R.id.agit_bookmark)

        fun bindItems(item: AgitSpaceData) {
            contentLayout.visibility = View.VISIBLE

            // 유저별 스페이스 이미지, 이름 설정
            spaceImage.setImageResource(item.spaceImg)
            spaceName.text = item.spaceName

            // 북마크 버튼 클릭시 로직
            bookmarkButton.setOnClickListener {
                // 북마크된 이미지와 북마크 해제된 이미지를 번갈아가면서 설정
                bookmarkButton.setImageResource(if (item.isBookmarked) R.drawable.agit_unbookmarked else R.drawable.agit_bookmarked)
                // 데이터 모델에서 북마크 상태를 업데이트
                item.isBookmarked = !item.isBookmarked
            }
        }
    }

    // 스켈레톤 UI에는 로딩화면만 존재하는 관계로 데이터가 불필요하므로 비활성화
    inner class SkeletonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

