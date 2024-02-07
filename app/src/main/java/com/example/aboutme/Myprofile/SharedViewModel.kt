package com.example.aboutme.Myprofile

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
    var nickname: String? = "다에몬"
    var selectedAvatarIndex: Int? = 3
    var selectedRoomIndex: Int? = 1
    var isCreated: Boolean = true

    // 댓글에 해당하는 텍스트를 저장할 변수
    var commentText: String? = null

    // 음악에 해당하는 텍스트를 저장할 변수
    var musicText: String? = null

    // 이야기에 해당하는 텍스트를 저장할 변수
    var storyText: String? = null

    // 감정에 해당하는 텍스트를 저장할 변수
    var feelingText: String? = null

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

    // EditText에 입력된 텍스트를 저장하는 함수
    fun saveText(text: String, variableName: String) {
        // variableName에 따라 적절한 변수에 텍스트를 저장합니다.
        when (variableName) {
            "commentText" -> commentText = text
            "musicText" -> musicText = text
            "storyText" -> storyText = text
            "feelingText" -> feelingText = text
            // 다른 변수에 대한 처리를 추가할 수 있습니다.
        }
    }
}