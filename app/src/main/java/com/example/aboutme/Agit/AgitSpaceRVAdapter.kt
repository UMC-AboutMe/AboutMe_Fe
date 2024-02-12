package com.example.aboutme.Agit

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.RetrofitMyspaceAgit.AgitSpaceData
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.AgitFavoriteResponse
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        private val agitFavorite: ImageView = itemView.findViewById(R.id.agit_bookmark)
        private val bookmarkButton: ImageView = itemView.findViewById(R.id.agit_bookmark)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]

                    // 데이터를 담은 Intent 생성
                    val intent = Intent(itemView.context, AgitMemberSpaceActivity::class.java)
                    intent.putExtra("spaceNameAgit", clickedItem.spaceName)
                    intent.putExtra("characterType", clickedItem.characterType)
                    intent.putExtra("roomType", clickedItem.roomType)
                    intent.putExtra("spaceId", clickedItem.spaceId)

                    // 다른 액티비티로 이동
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bindItems(item: AgitSpaceData) {
            contentLayout.visibility = View.VISIBLE

            // 유저별 스페이스 이미지, 이름, 북마크 여부 설정
            spaceImage.setImageResource(item.spaceImg)
            spaceName.text = item.spaceName
            if (item.isBookmarked) {
                agitFavorite.setImageResource(R.drawable.agit_bookmarked)
            } else {
                agitFavorite.setImageResource(R.drawable.agit_unbookmarked)
            }
            val spaceId = item.spaceId
            Log.d("spaceId", "$spaceId")

            // 북마크 버튼 클릭시 로직
            bookmarkButton.setOnClickListener {
                val call = RetrofitClient.apitest.agitFavorite(spaceId, "4")
                Log.d("spaceId", spaceId.toString())

                // 북마크된 이미지와 북마크 해제된 이미지를 번갈아가면서 설정
                bookmarkButton.setImageResource(if (item.isBookmarked) R.drawable.agit_unbookmarked else R.drawable.agit_bookmarked)
                // 데이터 모델에서 북마크 상태를 업데이트
                item.isBookmarked = !item.isBookmarked

                // 클릭할 때마다 변경된 북마크의 상태를 PATCH방식으로 전송
                call.enqueue(object : Callback<AgitFavoriteResponse> { // API 호출(call, response 데이터 클래스 명시)
                    override fun onResponse(call: Call<AgitFavoriteResponse>, response: Response<AgitFavoriteResponse>) {
                        if (response.isSuccessful) { // API 호출 성공시
                            val result = response.body()?.result
                            result?.let {
                            }
                        } else { // API 호출 실패시
                            handleApiError(response)
                        }
                    }

                    // API 호출 실패시
                    override fun onFailure(call: Call<AgitFavoriteResponse>, t: Throwable) {
                        handleApiFailure(t)
                    }
                })
            }
        }
    }

    private fun handleApiError(response: Response<AgitFavoriteResponse>) {
        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
    }

    // API ERROR 표시
    private fun handleApiFailure(t: Throwable) {
        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
    }

    // 스켈레톤 UI에는 로딩화면만 존재하는 관계로 데이터가 불필요하므로 비활성화
    inner class SkeletonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

