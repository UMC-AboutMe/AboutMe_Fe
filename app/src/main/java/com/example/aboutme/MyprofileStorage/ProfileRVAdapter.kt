package com.example.aboutme.MyprofileStorage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val profileImage = itemView.findViewById<ImageView>(R.id.avatar_iv)
            val profileName = itemView.findViewById<TextView>(R.id.profileName_tv)
            val profBasic = itemView.findViewById<ImageView>(R.id.prof_basic)
            val profFav = itemView.findViewById<ImageView>(R.id.prof_fav)

            profileImage.setImageResource(items.profile_img)
            profileName.text = items.profile_name

            //버튼의 가시성 변경
            profBasic.setOnClickListener {
                profBasic.visibility = View.GONE
                profFav.visibility =View.VISIBLE
                //favProfiles(adapterPosition)
                favProfiles()
            }
            profFav.setOnClickListener {
                profFav.visibility = View.GONE
                profBasic.visibility =View.VISIBLE
                //favProfiles(adapterPosition)
                favProfiles()
            }
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
    //프로필 보관함 즐겨찾기 등록
    private fun favProfiles() {
//        private fun favProfiles(position : Int) {
        Log.d("Retrofit_Fav", "patch 함수 호출됨")
        val call = ProfStorageObj.getRetrofitService.patchProfStorage(28, 1)
        //val call = ProfStorageObj.getRetrofitService.patchProfStorage(4, position+1)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseFavProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseFavProf>,
                response: Response<ProfStorageResponse.ResponseFavProf>
            ) {
                if (response.isSuccessful) { // HTTP 응답 코드가 200번대인지 여부 확인
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Fav", response.toString())
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

