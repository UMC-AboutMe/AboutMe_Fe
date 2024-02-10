package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultXX(
    @SerializedName("back_features")
    val backFeatures: List<BackFeatureXX>,
    @SerializedName("front_features")
    val frontFeatures: List<FrontFeatureXX>,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("profile_id")
    val profileId: Long,
    @SerializedName("profile_image")
    val profileImage: ProfileImage,
    @SerializedName("serial_number")
    val serialNumber: Int
)