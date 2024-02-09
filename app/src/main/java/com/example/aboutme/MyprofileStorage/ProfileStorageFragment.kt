package com.example.aboutme.MyprofileStorage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfilestorageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageFragment : Fragment() {

    lateinit var binding: FragmentProfilestorageBinding
    private val itemList = mutableListOf<ProfileData>()
    private lateinit var rvAdapter: ProfileRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilestorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        //val intent = Intent(requireContext(), ProfileStorageDetailFragment::class.java)

        //프로필 보관함 조회 api
        getProfiles()

        rvAdapter.setOnItemClickListener(object : ProfileRVAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("클릭2", "success")
                //startActivity(intent)
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                val fragment = ProfileStorageDetailFragment()
                fragmentTransaction.replace(R.id.detailLayout, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        })
    }

    private fun initRecycler() {
        rvAdapter = ProfileRVAdapter(itemList) // rvAdapter 초기화
        binding.profileStorageRv.adapter = rvAdapter
        binding.profileStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    //프로필 보관함 조회 api
    private fun getProfiles(){
        val call = ProfStorageObj.getRetrofitService.getProfStorage(1)

        call.enqueue(object : Callback<ProfStorageResponse.ResponeProfStorage> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponeProfStorage>,
                response: Response<ProfStorageResponse.ResponeProfStorage>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Get_Success", response.toString())
                            for (i in 1 .. response.result.total_member_profiles){
                                Log.d("Retrofit_Get_Success", "$i 프로필 조회")
                                itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필$i"))
                            }
                            rvAdapter.notifyDataSetChanged() //얘가 없으면 아이템이 갱신되지 않는다! 중요
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }
                    }
                }
                else {
                    Log.d("Retrofit_Get_Failed", response.toString())
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponeProfStorage>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Get_Error", errorMessage)
            }
        }
        )
    }
}

