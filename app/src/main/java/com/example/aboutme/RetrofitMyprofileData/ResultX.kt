package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultX(
    @SerializedName("back_features")
    val backFeatures: List<BackFeature>,
    @SerializedName("front_features")
    val frontFeatures: List<FrontFeature>,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("profile_id")
    val profileId: Int,
    @SerializedName("serial_number")
    val serialNumber: Int
)