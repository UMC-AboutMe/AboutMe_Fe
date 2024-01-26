package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aboutme.databinding.ActivityMyspacestep3Binding

class MySpaceStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 액티비티에서 받아온 인덱스 값
        val receivedIndexStep2avatar = intent.getIntExtra("index_step2_avatar", -1)
        val receivedIndexStep2room = intent.getIntExtra("index_step2_room", -1)

        Log.d("myspace_avatar", "$receivedIndexStep2avatar")
        Log.d("myspace_room", "$receivedIndexStep2room")

        // 이미지 리소스 이름을 동적으로 생성
        val imageNameavatar = "step2_avatar_${receivedIndexStep2avatar + 1}"
        val imageNameroom = "step3_room_${receivedIndexStep2room + 1}"

        // 리소스 아이디 가져오기
        val resourceIdavatar = resources.getIdentifier(imageNameavatar, "drawable", packageName)
        val resourceIdroom = resources.getIdentifier(imageNameroom, "drawable", packageName)

        binding.step3SelectedAvatar.setImageResource(resourceIdavatar)
        binding.step3SelectedRoom.setImageResource(resourceIdroom)

        binding.back.setOnClickListener {
            finish()
        }
    }
}