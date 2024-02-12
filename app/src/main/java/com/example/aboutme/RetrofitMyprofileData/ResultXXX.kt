package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultXXX(
    @SerializedName("feature_id")
    val featureId: Long,
    @SerializedName("feature_value")
    val featureValue: String?,
    @SerializedName("feature_key")
    val featureKey: String?
)