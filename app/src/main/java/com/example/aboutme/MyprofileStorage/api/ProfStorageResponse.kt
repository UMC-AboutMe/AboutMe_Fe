package com.example.aboutme.MyprofileStorage.api

class ProfStorageResponse {
    //서버에서 받아 올 응답
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
}