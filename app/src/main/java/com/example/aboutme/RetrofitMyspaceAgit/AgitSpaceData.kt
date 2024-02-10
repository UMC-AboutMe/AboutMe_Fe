package com.example.aboutme.RetrofitMyspaceAgit

data class AgitSpaceData(
    val spaceImg : Int,
    val spaceName : String,
    var isBookmarked : Boolean,
    val spaceId: Long,
    val characterType: Int,
    val roomType: Int
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
    val spaceId: Int,
    val nickname: String,
    val characterType: Int,
    val roomType: Int,
    val favorite: Boolean
)

data class ResultModelAdd(
    val spaceId: Int
)

data class AgitFavoriteResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: AgitFavoriteResult
)

data class AgitFavoriteResult(
    val favorite: Boolean
)

data class AgitMemberDelete(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)