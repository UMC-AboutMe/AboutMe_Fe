package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class BackFeature(
    @SerializedName("feature_id")
    val featureId: Int,
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)