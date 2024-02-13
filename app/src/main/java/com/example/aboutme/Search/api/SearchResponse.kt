package com.example.aboutme.Search.api

class SearchResponse {
    //상대방 마이프로필 내 보관함에 추가하기 - 바디
    data class RequestStoreProf (
        var profile_serial_numbers : List<Int>
            )

    //상대방 마이프로필 내 보관함에 추가하기 - 헤더
    data class ResponseStoreProf(
        var isSuccess : Boolean,
        var code : String,
        var message : String,
        var result : NumberList
    )
    data class NumberList(
        var member_id : Int
    )


    //스페이스 검색
    data class ResponseSearchSpace (
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result : SpaceList
            )
    data class SpaceList(
        var spaceId : Long,
        var nickname : String,
        var characterType : Int,
        var roomType : Int
    )

    //스페이스 보관함에 추가
    data class ResponseSpaceStorage (
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result : SpaceId
    )
    data class SpaceId (
        var spaceId : Long
            )

    //내 마이프로필 상대방에게 공유
    data class ResponseShareProf (
        var isSuccess: Boolean,
        val code: String,
        val message: String
            )
    data class RequestShareProf (
        var member_id : Int,
        var profile_serial_numbers: List<Int>
            )

    //내 마이프로필 조회(목록)
    data class ResponseGetProfiles(
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result: Result
    )

    data class Result(
        var myprofiles: List<Profile>,
        var total_myprofile: Int
    )

    data class Profile(
        var profile_id: Int,
        var serial_number: Int,
        var is_default: Boolean,
        var profile_image: ProfileImage,
        var front_features: List<FrontFeature>
    )

    data class ProfileImage(
        var type: String,
        var characterType: Int?,
        var profile_image_url: String?
    )

    data class FrontFeature(
        var key: String?,
        var value: String,
        var feature_id: Int
    )
}