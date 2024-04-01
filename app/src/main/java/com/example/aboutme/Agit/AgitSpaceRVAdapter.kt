package com.example.aboutme.Agit

import android.content.Context
import android.content.Intent
import android.content.res.Resources
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
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgitSpaceRVAdapter(private val resources: Resources, val items: MutableList<AgitSpaceData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var token: String // token 변수를 추가

    private fun getToken(context: Context): String? {
        val pref = context.getSharedPreferences("pref", 0)
        return pref.getString("Gtoken", null)
    }

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
        private val spaceName: TextView = itemView.findViewById(R.id.spaceName_tv)
        private val avatar: ImageView = itemView.findViewById(R.id.avatar_iv)
        private val room: ImageView = itemView.findViewById(R.id.space_iv)
        private val agitFavorite: ImageView = itemView.findViewById(R.id.agit_bookmark)
        private val bookmarkButton: ImageView = itemView.findViewById(R.id.agit_bookmark)

        init {
            token = getToken(context).toString()

            // 리사이클러뷰에 있는 멤버들의 스페이스를 클릭시
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]

                    // 멤버별 스페이스 액티비티로 이동
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

            // 유저별 이름, 아바타, 룸, 북마크 여부 설정
            // 룸, 아바타
            val selectedAvatarIndex = item.characterType
            val selectedRoomIndex = item.roomType

            val imageNameavatar = "agit_thumbnail_avatar_${selectedAvatarIndex}"
            val imageNameroom = "agit_thumbnail_room_${selectedRoomIndex}"

            val resourceIdAvatar = resources.getIdentifier(imageNameavatar, "drawable", context.packageName)
            val resourceIdRoom = resources.getIdentifier(imageNameroom, "drawable", context.packageName)

            avatar.setImageResource(resourceIdAvatar)
            room.setImageResource(resourceIdRoom)

            // 썸네일 이름
            spaceName.text = item.spaceName

            // 북마크
            if (item.isBookmarked) {
                agitFavorite.setImageResource(R.drawable.agit_bookmarked)
            } else {
                agitFavorite.setImageResource(R.drawable.agit_unbookmarked)
            }

            val spaceId = item.spaceId
            Log.d("spaceId", "$spaceId")

            // 북마크 버튼 클릭시 로직
            bookmarkButton.setOnClickListener {
                val call = RetrofitClient.apitest.agitFavorite(spaceId, token)
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

