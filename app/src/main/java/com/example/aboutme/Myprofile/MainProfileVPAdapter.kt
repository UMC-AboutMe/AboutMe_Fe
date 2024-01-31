package com.example.aboutme.Myprofile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutme.MyprofileStorage.ProfileStorageDetailActivity
import com.example.aboutme.databinding.ItemAddProfileBinding
import com.example.aboutme.databinding.ItemMultiprofileBinding
import com.kakao.sdk.template.model.Content
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class MainProfileVPAdapter : ListAdapter<FrontFeature, RecyclerView.ViewHolder>(
    MainListDiffCallback()
) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_ADD_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemMultiprofileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MainItemViewHolder(binding)
        } else {
            val binding =
                ItemAddProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            binding.profileAddBtn.setOnClickListener {
                Toast.makeText(parent.context, "프로필 추가", Toast.LENGTH_SHORT).show()

                val intent = Intent(parent.context, MainActivity2::class.java)
                parent.context.startActivity(intent)
            }
            MainAddItemViewHolder(binding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainItemViewHolder -> holder.bind(getItem(position) as MultiProfileData)
            is MainAddItemViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        // 마지막에 추가 항목을 위한 1을 더합니다.
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        // 마지막 항목인 경우 추가 항목을 위한 VIEW_TYPE_ADD_ITEM을 반환합니다.
        return if (position < super.getItemCount()) VIEW_TYPE_ITEM else VIEW_TYPE_ADD_ITEM
    }

    inner class MainItemViewHolder(private val binding: ItemMultiprofileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MultiProfileData) {
            Glide.with(binding.root.context)
                .load(item.profileImageUrl)
                .into(binding.multiProfileCharIv)
            binding.multiProfileNameTv.text = item.key
            binding.multiProfileNumberTv.text = item.phoneNumber

            RetrofitClient.mainProfile.getData().enqueue(object : Callback<FrontFeature> {
                // 서버 통신 실패 시의 작업
                override fun onFailure(call: Call<FrontFeature>, t: Throwable) {
                    Log.e("실패", t.toString())
                }

                // 서버 통신 성공 시의 작업
                // 매개변수 Response에 서버에서 받은 응답 데이터가 들어있음.
                override fun onResponse(
                    call: Call<FrontFeature>,
                    response: Response<FrontFeature>
                ) {
                    // 응답받은 데이터를 가지고 처리할 코드 작성
                    val repos: FrontFeature? = response.body()
                    if (repos != null) {
                        Log.d("성공", repos.toString())
                    } else {
                        Log.e("실패", "응답 데이터가 null입니다.")
                    }
                }
            })
        }


    }

    inner class MainAddItemViewHolder(private val binding: ItemAddProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // 추가 항목에 대한 바인딩 로직을 추가하세요.
        }
    }

    class MainListDiffCallback : DiffUtil.ItemCallback<MultiProfileData>() {
        override fun areItemsTheSame(
            oldItem: MultiProfileData,
            newItem: MultiProfileData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MultiProfileData,
            newItem: MultiProfileData
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }



}
