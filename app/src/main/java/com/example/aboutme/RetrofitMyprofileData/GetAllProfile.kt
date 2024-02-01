package com.example.aboutme.RetrofitMyprofileData

data class GetAllProfile(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: ResultXX
)