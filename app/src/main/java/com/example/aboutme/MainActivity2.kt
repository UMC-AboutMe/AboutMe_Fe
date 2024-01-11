package com.example.aboutme

import android.graphics.Insets.add
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow

class MainActivity2 : AppCompatActivity(R.layout.activity_main2) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                setReorderingAllowed(true)
                add(R.id.frame_container, ProfileStorageFragment())
            }
        }
    }
}
