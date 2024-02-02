package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class FrontFeatureXX(
    @SerializedName("feature_id")
    val feature_id: Long,
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)