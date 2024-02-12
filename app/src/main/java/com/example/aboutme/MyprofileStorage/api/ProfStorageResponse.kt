package com.example.aboutme.MyprofileStorage.api

class ProfStorageResponse {
    //서버에서 받아 올 응답
    //프로필 보관함 조회
    data class ResponeProfStorage (
        var isSuccess : Boolean,
        var code : String,
        var message : String,
        var result : ResultList
    )
    data class ResultList(
        var memberProfileList : List<Profiles>
    )
    data class Profiles (
        var profileId : Int,
        var profileName : String,
        var favorite : Boolean,
        var image : ImageDetail
    )
    data class ImageDetail (
        var type : String,
        var characterType : Int? ,
        var profile_image_url : String?
    )

    //프로필 보관함 특정 마이프로필 삭제
    data class ResponseDeleteProf (
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result : DeleteResultDetail
            )
    data class DeleteResultDetail(
        var msg : String,
        var memberProfileId : Int
    )

    //프로필 보관함 즐겨찾기 등록
    data class ResponseFavProf(
        val isSuccess: Boolean,
        val code: String,
        val message: String,
        val result: IsFavorite
    )
    data class IsFavorite(
        val favorite : Boolean
    )

    //프로필 보관함 검색
    data class ResponseSearchProf(
        val isSuccess: Boolean,
        val code: String,
        val message: String,
        val result: Result
    )
    data class Result(
        val memberProfileList: List<MemberProfile>
    )
    data class MemberProfile(
        val profileId: Int,
        val profileName: String,
        val favorite: Boolean,
        val image: Image
    )
    data class Image(
        val type: String,
        val characterType: Int?,
        val profile_image_url: String?
    )

}