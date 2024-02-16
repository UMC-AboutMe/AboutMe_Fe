package com.example.aboutme.Mypage.api

import com.example.aboutme.Search.api.SearchResponse

class MyPageResponse {
    data class ResponseMypage (
        var isSuccess : Boolean,
        var code : String,
        var result : Mypage
            )
    data class Mypage (
        var my_info : Info,
        var insight : Insight
            )
    data class Info (
        var profile_name : String,
        var space_name : String
            )
    data class Insight (
        var profile_shared_num : Int,
        var space_shared_num : Int
            )

    data class ResponseDeleteUser (
        var isSuccess : Boolean,
        var code : String,
        var result : Delete
            )
    data class Delete (
        var memberId : Long,
        var msg : String
            )
}