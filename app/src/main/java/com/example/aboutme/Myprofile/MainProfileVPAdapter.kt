package com.example.aboutme.Myprofile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.databinding.ItemAddProfileBinding
import com.example.aboutme.databinding.ItemMultiprofileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainProfileVPAdapter : ListAdapter<MultiProfileData, RecyclerView.ViewHolder>(
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

                RetrofitClient.mainProfile.getData().enqueue(object : Callback<MainProfileData> {
                    // 서버 통신 실패 시의 작업
                    override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                        Log.e("실패", t.toString())
                    }


                    override fun onResponse(
                        call: Call<MainProfileData>,
                        response: Response<MainProfileData>
                    ) {
                        val repos: MainProfileData? = response.body()
                        if (repos != null) {
                            val totalMyProfile = repos.getTotalMyProfile()

                            if (totalMyProfile > 2) {
                                val nameLimitDialog = NameLimitDialog()

                                nameLimitDialog.show((parent.context as AppCompatActivity).supportFragmentManager, nameLimitDialog.tag)
                            }
                            else{
                                val nameDialog = NameDialogFragment()

                                Log.d("!!!!","success")
                                nameDialog.show((parent.context as AppCompatActivity).supportFragmentManager, nameDialog.tag)
                            }
                        } else {
                            Log.e("실패", "front_features 데이터가 null입니다.")
                        }
                    }
                })

            }
            MainAddItemViewHolder(binding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainItemViewHolder -> {
                val item = getItem(position)
                Log.d("MainProfileVPAdapter", "Binding item at position $position: $item")
                holder.bind(item)
            }
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

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("뷰페이저!",position.toString())
                    if (position == 0){
                        val intent = Intent(itemView.context, MainActivity2::class.java)
                        intent.putExtra("positionId",0)
                        itemView.context.startActivity(intent)
                    }
                    if (position == 1){
                        val intent = Intent(itemView.context, MainActivity2::class.java)
                        intent.putExtra("positionId",1)
                        itemView.context.startActivity(intent)


                    }
                    if (position == 2){
                        val intent = Intent(itemView.context, MainActivity2::class.java)
                        intent.putExtra("positionId",2)
                        itemView.context.startActivity(intent)

                    }

                }
            }
        }
        fun bind(item: MultiProfileData) {
            binding.multiProfileCharIv.setImageResource(item.profileImageResId)
            binding.multiProfileNameTv.text = item.name
            binding.multiProfileNumberTv.text = item.phoneNumber
        }
    }

    inner class MainAddItemViewHolder(private val binding: ItemAddProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                Log.d("뷰페이저!!",position.toString())
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("뷰페이저!!",position.toString())

                }
            }
        }
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

    override fun getItemId(position: Int): Long {
        // 아이템의 위치(position)을 그대로 ID로 사용
        return position.toLong()
    }



}