package com.example.aboutme.RetrofitMyprofileData

data class RequestPatchProfile(
    val feature_id: Long,
    val feature_key: String,
    val feature_value: String
)
