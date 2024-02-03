package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class PatchMyprofile(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: ResultXXX
)