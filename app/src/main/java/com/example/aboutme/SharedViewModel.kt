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
}