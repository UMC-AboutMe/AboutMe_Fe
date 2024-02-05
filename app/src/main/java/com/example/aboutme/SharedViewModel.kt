package com.example.aboutme

import android.net.Uri
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){

    val profileLayoutLiveData = MutableLiveData<View>()
    val storeBitmap = MutableLiveData<Boolean>()
    val savedImageUri = MutableLiveData<Uri?>()

    // 마이스페이스 관련 데이터
    var nickname: String? = null
    var selectedAvatarIndex: Int? = null
    var selectedRoomIndex: Int? = null
    var isCreated: Boolean = false

    fun storeProfileLayout(profileLayout: View) {
        profileLayoutLiveData.value = profileLayout
        setStoreBitmap(true)
    }

    fun setStoreBitmap(value: Boolean) {
        storeBitmap.value = value
    }

    fun setSavedImageUri(uri: Uri) {
        savedImageUri.value = uri
    }

    fun getSavedImageUri(): Uri? {
        return savedImageUri.value
    }

    // 선택된 Avatar의 인덱스를 저장하는 메서드
    fun setSelectedAvatarIndex(index: Int) {
        selectedAvatarIndex = index
    }

    fun setSelectedRoomIndex(index: Int) {
        selectedRoomIndex = index
    }
}