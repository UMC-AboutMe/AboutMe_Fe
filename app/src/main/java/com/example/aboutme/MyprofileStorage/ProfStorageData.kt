package com.example.aboutme.MyprofileStorage

import com.google.gson.annotations.SerializedName

data class ProfStorageData(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
)

data class Result(
    @SerializedName("favorite") val favorite: Boolean
)

