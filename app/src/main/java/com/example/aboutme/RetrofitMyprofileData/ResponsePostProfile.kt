package com.example.aboutme.RetrofitMyprofileData

data class ResponsePostProfile(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: ResultX
)