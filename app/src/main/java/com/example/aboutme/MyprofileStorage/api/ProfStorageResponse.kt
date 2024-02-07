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
        var memberProfile : List<Profiles>,
        var totalProfiles : Int
    )
    data class Profiles (
        var member_profile_id : Int,
        var favorite : Boolean,
        var member : MemberDetail,
        var profile : ProfileDetail
    )
    data class MemberDetail (
        var social : String,
        var email : String
    )
    data class ProfileDetail (
        var profile_id : Int,
        var serial_number : Int,
        var is_default : Boolean
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
}