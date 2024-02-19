package com.example.aboutme.Myprofile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.databinding.ItemAddProfileBinding
import com.example.aboutme.databinding.ItemMultiprofileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainProfileVPAdapter(private val context: Context) : ListAdapter<MultiProfileData, RecyclerView.ViewHolder>(
    MainListDiffCallback()
) {

    val pref = context.getSharedPreferences("pref", 0)
    val token = pref.getString("Gtoken", null) ?: ""

    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_ADD_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.d("token3", token)


        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemMultiprofileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            MainItemViewHolder(binding)
        } else {
            val binding =
                ItemAddProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            binding.profileAddBtn.setOnClickListener {

                RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {


                    // 서버 통신 실패 시의 작업
                    override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                        Log.e("실패", t.toString())
                    }


                    override fun onResponse(
                        call: Call<MainProfileData>,
                        response: Response<MainProfileData>
                    ) {
                        if (response.isSuccessful) {
                            val repos: MainProfileData? = response.body()
                            if (repos != null) {
                                val totalMyProfile = repos.getTotalMyProfile()

                                if (totalMyProfile > 2) {
                                    Log.d("MainProfileVPAdapter", "Total profiles count: $totalMyProfile")
                                    val nameLimitDialog = NameLimitDialog()
                                    nameLimitDialog.show((parent.context as AppCompatActivity).supportFragmentManager, nameLimitDialog.tag)
                                } else {
                                    Log.d("MainProfileVPAdapter", "Total profiles count: $totalMyProfile")
                                    val nameDialog = NameDialogFragment()
                                    Log.d("MainProfileVPAdapter", "Showing name dialog")
                                    nameDialog.show((parent.context as AppCompatActivity).supportFragmentManager, nameDialog.tag)
                                }
                            } else {
                                Log.e("MainProfileVPAdapter", "Response body is null")
                            }
                        } else {
                            Log.e("MainProfileVPAdapter", "Unsuccessful response: ${response.code()}")
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

                /*if (item.isDefault) {
                    holder.binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                } else {
                    holder.binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                }*/
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

        private val profBasic: ImageView = itemView.findViewById(R.id.defaultNoProfile_btn)
        private val profFav: ImageView = itemView.findViewById(R.id.defaultProfile_btn)


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


            RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {
                override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                    Log.e("실패", t.toString())
                }

                override fun onResponse(call: Call<MainProfileData>, response: Response<MainProfileData>) {
                    if (response.isSuccessful) {
                        val repos: MainProfileData? = response.body()
                        if (repos != null) {

                        } else {
                            Log.e("실패", "응답 데이터가 null입니다.")
                        }
                    } else {
                        Log.e("실패", "서버 응답 실패: ${response.code()}")
                    }
                }
            })



            /*            profilePosion(position) { realProfileId ->
                            Log.d("realprofileID!", realProfileId.toString())


                            coroutineScope.launch {
                                try {
                                    val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                                        RetrofitClient.mainProfile.getDataAll(token, realProfileId.toLong())
                                    }

                                    if (response.isSuccessful) {
                                        val responseData: GetAllProfile? = response.body()
                                        Log.d("즐겨찾기 취소!!", "응답 데이터: $responseData")

                                        if (responseData?.result?.isDefault == false) {
                                            //patchDefault(realProfileId)
                                            Log.d("즐겨찾기#3",responseData?.result?.isDefault.toString())

                                            binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                        } else {
                                            //defaultNoProfile(realProfileId)
                                            Log.d("즐겨찾기#4",responseData?.result?.isDefault.toString())

                                            binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        }
                                        val errorBody = response.errorBody()?.string() ?: "No error body"
                                        Log.e(
                                            "GETALL 요청 실패",
                                            "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                                        )
                                    }
                                } catch (e: Exception) {
                                    Log.e("GETALL 요청 실패", "에러: ${e.message}")
                                }
                            }
                        }*/

        }
        fun bind(item: MultiProfileData) {
            if (item.profileImageResId.startsWith("http")) {
                Glide.with(itemView.context)
                    .load(item.profileImageResId)
                    .into(binding.multiProfileCharIv)
            } else {
                binding.multiProfileCharIv.setImageResource(item.profileImageResId.toInt())
            }
            binding.multiProfileNameTv.text = item.name
            binding.multiProfileNumberTv.text = item.phoneNumber


            profilePosion(position) { realProfileId ->
                Log.d("realprofileID!", realProfileId.toString())



                    coroutineScope.launch {
                        try {
                            val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                                RetrofitClient.mainProfile.getDataAll(token, realProfileId.toLong())
                            }

                            if (response.isSuccessful) {
                                val responseData: GetAllProfile? = response.body()


                                if (responseData!!.result.isDefault) {
                                    profBasic.visibility = View.GONE
                                    profFav.visibility = View.VISIBLE
                                } else {
                                    profBasic.visibility = View.VISIBLE
                                    profFav.visibility = View.GONE
                                }

                                profBasic.setOnClickListener {
                                    profBasic.visibility = View.GONE
                                    profFav.visibility = View.VISIBLE
                                    patchDefault(realProfileId)
                                }
                                profFav.setOnClickListener {
                                    profFav.visibility = View.GONE
                                    profBasic.visibility = View.VISIBLE
                                    defaultNoProfile(realProfileId)
                                }

                            }
                        } catch (e: Exception) {
                            Log.e("GETALL 요청 실패", "에러: ${e.message}")
                        }
                    }


                /*RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {
                    override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                        Log.e("실패", t.toString())
                    }

                    override fun onResponse(call: Call<MainProfileData>, response: Response<MainProfileData>) {
                        if (response.isSuccessful) {
                            val repos: MainProfileData? = response.body()
                            if (repos != null) {
                                // 성공적으로 데이터를 받아온 경우 처리할 로직을 여기에 추가합니다.
                                if (repos.result.totalMyprofile == 1){
                                    if (repos.result.myprofiles[0].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        Log.d("즐겨찾기#1",repos.result.myprofiles[0].isDefault.toString())
                                    }
                                    if (repos.result.myprofiles[0].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                        Log.d("즐겨찾기#3",repos.result.myprofiles[0].isDefault.toString())

                                    }
                                }else if (repos.result.totalMyprofile == 2){
                                    if (repos.result.myprofiles[0].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        Log.d("즐겨찾기#1",repos.result.myprofiles[0].isDefault.toString())
                                    }
                                    if (repos.result.myprofiles[1].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        Log.d("즐겨찾기#2",repos.result.myprofiles[1].isDefault.toString())

                                    }
                                    if (repos.result.myprofiles[0].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                        Log.d("즐겨찾기#3",repos.result.myprofiles[0].isDefault.toString())

                                    }
                                    if (repos.result.myprofiles[1].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                    }
                                } else if (repos.result.totalMyprofile == 3){
                                    if (repos.result.myprofiles[0].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        Log.d("즐겨찾기#1",repos.result.myprofiles[0].isDefault.toString())
                                    }
                                    if (repos.result.myprofiles[1].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                        Log.d("즐겨찾기#2",repos.result.myprofiles[1].isDefault.toString())

                                    }
                                    if (repos.result.myprofiles[2].isDefault == false){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                                    }
                                    if (repos.result.myprofiles[0].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                        Log.d("즐겨찾기#3",repos.result.myprofiles[0].isDefault.toString())

                                    }
                                    if (repos.result.myprofiles[1].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                    }
                                    if (repos.result.myprofiles[2].isDefault == true){
                                        binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                                    }
                                }
                            } else {
                                Log.e("실패", "응답 데이터가 null입니다.")
                            }
                        } else {
                            Log.e("실패", "서버 응답 실패: ${response.code()}")
                        }
                    }
                })
*/



                /*coroutineScope.launch {
                    try {
                        val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                            RetrofitClient.mainProfile.getDataAll(token, realProfileId.toLong())
                        }

                        if (response.isSuccessful) {
                            val responseData: GetAllProfile? = response.body()
                            Log.d("즐겨찾기 취소!!", "응답 데이터: $responseData")

                            if (responseData?.result?.isDefault == false) {
                                //patchDefault(realProfileId)
                                Log.d("즐겨찾기#1",responseData?.result?.isDefault.toString())
                                binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                            }else{
                                //defaultNoProfile(realProfileId)
                                Log.d("즐겨찾기#2",responseData?.result?.isDefault.toString())
                                binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)
                            }
                            val errorBody = response.errorBody()?.string() ?: "No error body"
                            Log.e(
                                "GETALL 요청 실패",
                                "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("GETALL 요청 실패", "에러: ${e.message}")
                    }
                }*/
            }

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


    private fun profilePosion(positionId: Int, callback: (Int) -> Unit) {
        var realProfileId = -1 // 기본값 설정

        Log.d("token2", token)

        RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {
            // 서버 통신 실패 시의 작업
            override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                Log.e("실패", t.toString())
                callback(realProfileId) // 실패 시에도 콜백 호출
            }

            override fun onResponse(
                call: Call<MainProfileData>,
                response: Response<MainProfileData>
            ) {
                val repos: MainProfileData? = response.body()
                if (repos != null) {
                    val totalMyProfile = repos.getTotalMyProfile()
                    Log.d("get!!", "응답 데이터: $repos")

                    if (totalMyProfile == 1) {
                        realProfileId = repos.result.myprofiles[0].profileId
                    }
                    if (totalMyProfile == 2) {
                        val minProfileId = repos.result.myprofiles[0].profileId
                        val maxProfileId = repos.result.myprofiles[1].profileId

                        realProfileId = if (positionId == 0) {
                            minProfileId
                        } else {
                            maxProfileId
                        }
                    }
                    if (totalMyProfile == 3) {
                        val minProfileId = repos.result.myprofiles[0].profileId
                        val mediumProfileId = repos.result.myprofiles[1].profileId
                        val maxProfileId = repos.result.myprofiles[2].profileId

                        realProfileId = when {
                            positionId == 0 -> minProfileId
                            positionId == 1 -> mediumProfileId
                            else -> maxProfileId
                        }
                    }
                } else {
                    Log.e("실패", "서버 응답 실패: ${response.code()}")
                }
                callback(realProfileId) // 응답 처리 후에 콜백 호출
            }
        })
    }

    private fun patchDefault(realProfileId : Int){
        coroutineScope.launch {
            try {
                val response = RetrofitClient.mainProfile.patchDefaultProfile(token,realProfileId.toLong())
                if (response.isSuccessful) {
                    // 성공적으로 응답을 받았을 때 처리
                    val data = response.body()
                    Log.d("즐겨찾기 취소!",data.toString())
                //binding.defaultNoProfileBtn.setImageResource(R.drawable.default10)
                // data를 사용하여 필요한 작업을 수행
                } else {

                    Log.e("Failure", "Response failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 발생 시 처리
                e.printStackTrace()
                Log.d("패치!!","실패")
            }
        }
    }


    private fun defaultNoProfile(realProfileId: Int){
        coroutineScope.launch {
            try {
                val response = RetrofitClient.mainProfile.patchNoDefaultProfile(token, realProfileId.toLong())
                if (response.isSuccessful){
                    val data = response.body()
                    Log.d("즐겨찾기 취소", data.toString())
                    //binding.defaultNoProfileBtn.setImageResource(R.drawable.nodefault)

                } else{
                    Log.e("Failure~!", "Response failed with code: ${response.code()}")
                }
            }catch (e: Exception) {
                // 네트워크 오류 등 예외 발생 시 처리
                e.printStackTrace()
                Log.d("패치!!","실패")
            }
        }
    }
}