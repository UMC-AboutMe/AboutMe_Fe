package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultX(
    @SerializedName("back_features")
    val BackFeatures: List<BackFeature>,
    @SerializedName("front_features")
    val FrontFeatures: List<FrontFeature>,
    @SerializedName("is_default")
    val IsDefault: Boolean,
    @SerializedName("profile_id")
    val ProfileId: Int,
    @SerializedName("serial_number")
    val SerialNumber: Int
)