package com.example.aboutme.Myprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FrontProfileViewModel : ViewModel() {

    private val _editName = MutableLiveData<String>()

    val editName : LiveData<String> = _editName

    fun setEditName(name : String){
        _editName.value = name
    }


}