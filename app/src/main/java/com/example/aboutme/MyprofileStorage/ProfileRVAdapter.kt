package com.example.aboutme.MyprofileStorage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRVAdapter(val items: MutableList<ProfileData>) :
    RecyclerView.Adapter<ProfileRVAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profilestorage, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val profileImage: ImageView = itemView.findViewById(R.id.avatar_iv)
        private val profileName: TextView = itemView.findViewById(R.id.profileName_tv)
        private val profBasic: ImageView = itemView.findViewById(R.id.prof_basic)
        private val profFav: ImageView = itemView.findViewById(R.id.prof_fav)

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItems(profileData: ProfileData) {
            profileImage.setImageResource(profileData.profile_img)
            profileName.text = profileData.profile_name

            profBasic.setOnClickListener {
                profBasic.visibility = View.GONE
                profFav.visibility = View.VISIBLE
                favProfiles(profileData.profile_id, 6)
            }
            profFav.setOnClickListener {
                profFav.visibility = View.GONE
                profBasic.visibility = View.VISIBLE
                favProfiles(profileData.profile_id, 6)
            }
        }

        override fun onClick(view: View) {
            val pos = adapterPosition
            Log.d("ProfileRVAdapter", "Item clicked at position $pos")
            if (pos != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(view, pos)
            }
        }
    }
//보관함 즐겨찾기
    private fun favProfiles(profId: Long, memberId: Int) {
        Log.d("Retrofit_Fav", "patch 함수 호출됨")
        val call = ProfStorageObj.getRetrofitService.patchProfStorage(profId, memberId)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseFavProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseFavProf>,
                response: Response<ProfStorageResponse.ResponseFavProf>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Fav", responseBody.toString())
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Fav", "처리에 실패함")
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseFavProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Fav", errorMessage)
            }
        })
    }
}
