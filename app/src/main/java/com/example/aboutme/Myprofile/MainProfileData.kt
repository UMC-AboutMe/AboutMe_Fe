package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName

data class MainProfileData(
    @SerializedName("code")
    val code: String,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Result
)