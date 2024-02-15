package com.example.aboutme.Myprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.ResultXX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> get() = _phoneNumber

    fun setNameAndPhoneNumber(name: String, phoneNumber: String) {
        _name.value = name
        _phoneNumber.value = phoneNumber
    }

    private val _feature1 = MutableLiveData<String>()
    val feature1:LiveData<String> get() = _feature1

    private val _feature2 = MutableLiveData<String>()
    val feature2:LiveData<String> get() = _feature2

    private val _feature3 = MutableLiveData<String>()
    val feature3:LiveData<String> get() = _feature3

    private val _feature4 = MutableLiveData<String>()
    val feature4:LiveData<String> get() = _feature4

    private val _feature5 = MutableLiveData<String>()
    val feature5:LiveData<String> get() = _feature5

    fun setFeatures(feature1: String,feature2: String,feature3: String,feature4: String,feature5: String){
        _feature1.value = feature1
        _feature2.value = feature2
        _feature3.value = feature3
        _feature4.value = feature4
        _feature5.value = feature5
    }

}