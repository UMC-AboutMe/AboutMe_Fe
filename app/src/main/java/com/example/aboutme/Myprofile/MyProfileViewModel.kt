package com.example.aboutme.Myprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile

class MyProfileViewModel : ViewModel() {
    // 라이브 데이터 선언
    private val _updatedData: MutableLiveData<GetAllProfile?> by lazy {
        MutableLiveData<GetAllProfile?>()
    }

    // 수정된 데이터를 외부에서 읽을 수 있도록 하는 LiveData
    val updatedData: LiveData<GetAllProfile?> = _updatedData

    // 데이터를 업데이트하는 메서드
    fun updateData(data: GetAllProfile?) {
        _updatedData.value = data
    }

}