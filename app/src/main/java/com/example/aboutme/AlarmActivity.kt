package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutme.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //최근 7일 알림
        val itemList = ArrayList<Alarm_day7>()
        itemList.add(Alarm_day7(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemList.add(Alarm_day7(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))
        itemList.add(Alarm_day7(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemList.add(Alarm_day7(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))
        itemList.add(Alarm_day7(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemList.add(Alarm_day7(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))

        val AlarmDay7Adapter = AlarmDay7Adapter(itemList)
        AlarmDay7Adapter.notifyDataSetChanged()
        binding.day7Rc.adapter = AlarmDay7Adapter
        binding.day7Rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //예전 알림
        val itemListOld = ArrayList<Alarm_old>()
        itemListOld.add(Alarm_old(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemListOld.add(Alarm_old(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))
        itemListOld.add(Alarm_old(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemListOld.add(Alarm_old(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))
        itemListOld.add(Alarm_old(R.drawable.nav_myprof,"테디님이 프로필을 공유하였습니다."))
        itemListOld.add(Alarm_old(R.drawable.nav_myspace,"테디님이 스페이스를 공유하였습니다."))

        val AlarmOldAdapter = AlarmDay7Adapter(itemList)
        AlarmOldAdapter.notifyDataSetChanged()
        binding.oldRc.adapter = AlarmOldAdapter
        binding.oldRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }
}