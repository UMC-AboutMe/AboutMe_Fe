package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName

data class FrontFeature(
    @SerializedName("feature_id")
    val featureId: Int,
    @SerializedName("profile_id")
    val profileId: Int,
    @SerializedName("key")
    val key: String?,
    @SerializedName("value")
    val value: String?
)