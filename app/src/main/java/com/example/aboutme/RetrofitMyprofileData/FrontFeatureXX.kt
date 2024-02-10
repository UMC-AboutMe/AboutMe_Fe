package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class FrontFeatureXX(
    @SerializedName("feature_id")
    val featureId: Long,
    @SerializedName("key")
    val key: String?,
    @SerializedName("value")
    val value: String?
)