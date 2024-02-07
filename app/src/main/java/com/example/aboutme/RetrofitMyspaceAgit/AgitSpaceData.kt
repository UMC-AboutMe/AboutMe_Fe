package com.example.aboutme.RetrofitMyspaceAgit

data class AgitSpaceData(
    val spaceImg : Int,
    val spaceName : String,
    var isBookmarked : Boolean = false
)

data class YourResponseType(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultModel
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