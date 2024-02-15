package com.example.aboutme.RetrofitMyprofileData

import com.google.gson.annotations.SerializedName

data class ResultXXXX(
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("profile_id")
    val profile_id: Long,
    @SerializedName("serial_number")
    val serial_number: Int
)