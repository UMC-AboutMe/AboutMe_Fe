package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityTutorial3Binding

class TutorialActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityTutorial3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial3)
        binding.okIv2.setOnClickListener {

            Toast.makeText(this, "okclicked", Toast.LENGTH_SHORT).show()

            val intent= Intent(this, MySpaceStep1Activity::class.java)
            startActivity(intent)

        }

        binding.cb2.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked){
                true -> Toast.makeText(this, "체크됨", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(this, "체크해제됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

}