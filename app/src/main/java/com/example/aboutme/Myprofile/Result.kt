package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("myprofiles")
    val myprofiles: List<Myprofile>,
    @SerializedName("total_myprofile")
    val totalMyprofile: Int
)