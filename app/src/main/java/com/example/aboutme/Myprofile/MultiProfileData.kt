package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName


data class MultiProfileData(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("profile_img_url")
    val profileImageUrl: String?
)

