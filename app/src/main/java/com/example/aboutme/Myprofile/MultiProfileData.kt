package com.example.aboutme.Myprofile

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("profile_id")
    val profileId: Int,
    @SerializedName("profile_img_url")
    val profileImageUrl: String?, // 이미지 URL은 nullable일 수 있습니다.
    val frontFeatures: List<MultiProfileData>
)

data class MultiProfileData(
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("profile_img_url")
    val profileImageUrl: String?
    // 추가적인 필드가 있을 수 있습니다.
)

data class MyProfilesResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
)

data class Result(
    @SerializedName("myprofiles")
    val myProfiles: List<Profile>,
    @SerializedName("total_myprofile")
    val totalMyProfile: Int
)
