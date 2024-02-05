package com.example.aboutme

data class AgitSpaceData(
    val space_img : Int,
    val space_name : String
)

data class YourResponseType(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultModelAdd
)

data class ResultModel(
    val memberSpaceList: List<SpaceModel>
)

data class SpaceModel(
    val space_id: Int,
    val nickname: String,
    val characterType: Int,
    val roomType: Int,
    val favorite: Boolean
)

data class ResultModelAdd(
    val spaceId: Int
)