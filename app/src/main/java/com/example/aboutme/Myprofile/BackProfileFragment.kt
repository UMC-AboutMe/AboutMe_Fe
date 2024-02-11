package com.example.aboutme.Myprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.FragmentBackprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BackProfileFragment : Fragment()
{

    companion object {
        // newInstance 메서드 추가
        fun newInstance(positionId: Int): BackProfileFragment {
            val fragment = BackProfileFragment()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding : FragmentBackprofileBinding

    private var selectedButtonId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentBackprofileBinding.inflate(inflater, container, false)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("FrontProfileFragment!!", "Profile ID: $positionId")

        /*binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
        }*/

        binding.turnBtn2.setOnClickListener {
            // 프로필 ID 가져오기
            val positionId = arguments?.getInt("positionId", -1)

            // BackProfileFragment로 전환하기 위해 프로필 ID를 번들에 담아서 생성
            val frontProfileFragment = FrontProfileFragment.newInstance(positionId ?: -1)

            // 프로필 ID를 담은 번들을 BackProfileFragment로 전달
            frontProfileFragment.arguments = arguments

            // BackProfileFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_frame, frontProfileFragment)
                .commit()
        }





        return binding.root

    }
}