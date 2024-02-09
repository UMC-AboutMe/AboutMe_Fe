package com.example.aboutme.RetrofitMyspaceAgit

//data class YourResponseType(
//    val isSuccess: Boolean,
//    val code: String,
//    val message: String,
//    val result: ResultModel
//)
//
//data class ResultModel(
//    val memberSpaceList: List<SpaceModel>
//)
//
//data class SpaceModel(
//    val space_id: Int,
//    val nickname: String,
//    val characterType: Int,
//    val roomType: Int,
//    val favorite: Boolean
//)

data class MySpaceCreateRequest (
    val nickname: String,
    val characterType: Int,
    val roomType: Int
)

data class MySpaceCreate (
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultModelmsc
)

data class ResultModelmsc (
    val nickname: String,
    val characterType: Int,
    val roomType: Int
)