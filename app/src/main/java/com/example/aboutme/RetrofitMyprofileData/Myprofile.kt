package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class Myprofile(
    @SerializedName("front_features")
    val frontFeatures: List<FrontFeature>,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("myprofile_id")
    val profileId: Int,
    @SerializedName("profile_image")
    val profileImage: ProfileImage,
    @SerializedName("serial_number")
    val serialNumber: Int
)