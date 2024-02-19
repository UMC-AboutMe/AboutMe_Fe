package com.example.aboutme.Agit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.AgitFavoriteResponse
import com.example.aboutme.RetrofitMyspaceAgit.AgitMemberDelete
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient
import com.example.aboutme.Search.CustomDialog
import com.example.aboutme.databinding.ActivityAgitMemberSpaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgitMemberSpaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgitMemberSpaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgitMemberSpaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spaceNameTV: TextView = binding.myspaceTitle

        // AgitSpaceRVAdapter에서 전송한 intent를 전달받는다
        val spaceName = intent.getStringExtra("spaceNameAgit")
        val characterType = intent.getIntExtra("characterType", -1)
        val roomType = intent.getIntExtra("roomType", -1)
        val spaceId = intent.getIntExtra("spaceId", -1)

        Log.d("YourActivity", "Received spaceName: $spaceName")
        Log.d("YourActivity", "Received characterType: $characterType")
        Log.d("YourActivity", "Received roomType: $roomType")
        Log.d("YourActivity", "Received roomType: $intent")

        val index = spaceName?.indexOf("'s")
        if (index != -1) {
            val trimmedSpaceName = index?.let { spaceName.substring(0, it + 2) }
            spaceNameTV.text = trimmedSpaceName
        } else {
            spaceNameTV.text = spaceName
        }

        // 각종 리소스 설정
        val imageCharacterId = when(characterType) {
            1 -> R.drawable.step2_avatar_1
            2 -> R.drawable.step2_avatar_2
            3 -> R.drawable.step2_avatar_3
            4 -> R.drawable.step2_avatar_4
            5 -> R.drawable.step2_avatar_5
            6 -> R.drawable.step2_avatar_6
            7 -> R.drawable.step2_avatar_7
            8 -> R.drawable.step2_avatar_8
            9 -> R.drawable.step2_avatar_9
            else -> {R.drawable.step2_avatar_1}
        }
        binding.step3SelectedAvatar.setImageResource(imageCharacterId)

        val imageRoomId = when(roomType) {
            1 -> R.drawable.step3_smallroom_1
            2 -> R.drawable.step3_smallroom_2
            3 -> R.drawable.step3_smallroom_3
            4 -> R.drawable.step3_smallroom_4
            else -> {R.drawable.step3_smallroom_1}
        }
        binding.step3SelectedRoom.setImageResource(imageRoomId)

        // 화면 상단의 로고 클릭시 화면 종료
        binding.logo.setOnClickListener {
            finish()
        }

        binding.agitMemberspaceTrash.setOnClickListener {
            val spaceIdLong = spaceId.toLong()

            CustomDialogAgit("${spaceName}를 삭제하시겠습니까?", spaceIdLong).show(supportFragmentManager, "AgitDialog")
        }
    }
}
