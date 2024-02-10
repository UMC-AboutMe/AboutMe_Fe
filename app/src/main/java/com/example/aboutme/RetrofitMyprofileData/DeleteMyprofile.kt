package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class DeleteMyprofile(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String
)