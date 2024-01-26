package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        val layouts = listOf(
            binding.step3FeelingLayout,
            binding.step3CommentLayout,
            binding.step3StoryLayout,
            binding.step3MusicLayout,
            binding.step3ScheduleLayout,
            binding.step3PhotoLayout
        )

        val buttons = listOf(
            binding.step3Feeling,
            binding.step3Comment,
            binding.step3Story,
            binding.step3Music,
            binding.step3Schedule,
            binding.step3Photo
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // 기존에 선택된 레이아웃들을 모두 숨김
                layouts.forEach { layout -> layout.visibility = View.GONE }

                // 현재 클릭된 버튼에 해당하는 레이아웃을 보임
                layouts[index].visibility = View.VISIBLE
            }
        }

    }
}