package com.example.aboutme.RetrofitMyspaceAgit

data class MyspaceCheckResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: MyspaceCheckResult
)

data class MyspaceCheckResult(
    val nickname: String,
    val characterType: Int,
    val roomType: Int,
    val mood: String,
    val musicUrl: String,
    val statusMessage: String,
    val spaceImageList: List<ImageList>,
    val planList: List<PlanList>
)

data class ImageList(
    val content: String,
    val date: String
)

data class PlanList(
    val content: String,
    val date: String
)