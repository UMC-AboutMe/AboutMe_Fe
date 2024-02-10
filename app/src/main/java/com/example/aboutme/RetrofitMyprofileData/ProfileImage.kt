package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ProfileImage(
    @SerializedName("type")
    val type : String,
    @SerializedName("character_type")
    val characterType : String,
    @SerializedName("profile_image_url")
    val profileImageUrl : String
)
