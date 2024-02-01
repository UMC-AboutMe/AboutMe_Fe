package com.example.aboutme.RetrofitMyprofileData

data class ResultXX(
    val back_features: List<BackFeatureXX>,
    val front_features: List<FrontFeatureXX>,
    val is_default: Boolean,
    val profile_id: Int,
    val profile_img_url: Any,
    val serial_number: Int
)