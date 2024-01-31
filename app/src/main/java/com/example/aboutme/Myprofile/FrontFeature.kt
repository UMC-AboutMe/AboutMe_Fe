package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName

data class FrontFeature(
    @SerializedName("feature_id")
    val featureId: Int,
    @SerializedName("key")
    val key: String?, // 기본값을 빈 문자열로 설정
    @SerializedName("value")
    val value: String?// 기본값을 빈 문자열로 설정
)