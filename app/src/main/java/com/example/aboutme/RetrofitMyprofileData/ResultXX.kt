package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultXX(
    @SerializedName("back_features")
    val back_features: List<BackFeatureXX>,
    @SerializedName("front_features")
    val front_features: List<FrontFeatureXX>,
    @SerializedName("is_default")
    val is_default: Boolean,
    @SerializedName("profile_id")
    val profile_id: Long,
    @SerializedName("profile_img_url")
    val profile_img_url: String,
    @SerializedName("serial_number")
    val serial_number: Int
)