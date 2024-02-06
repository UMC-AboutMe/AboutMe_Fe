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
        var profileSerialNumberList : String
    )
}