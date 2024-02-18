package com.example.aboutme.Alarm.api

class AlarmResponse {
    //알람 조회하기
    data class ResponseAlarm(
        var isSuccess : Boolean,
        var code : String,
        var result : AlarmResult
    )
    data class AlarmResult (
        var alarms : List<Alarms>
            )
    data class Alarms (
        var alarm_id : Long,
        var content : String,
        var profile_serial_number : Int,
        var space_id : Long
    )
    //마이프로필 보관함에 추가하기
    data class ResponseStorageProf (
        var isSuccess: Boolean,
        var code: String,
        var message : String,
        var result : Result?
            )
    data class Result (
        var profileSerialNumberList : String
            )
    data class RequestStorageProf(
        var profile_serial_numbers : List<Int>
    )
    //스페이스 아지트에 추가하기
    data class ResponseStorageSpace (
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result : Result2?
            )
    data class Result2 (
        var spaceId : Int
            )
    //알림 데이터 삭제
    data class ResponseDeleteAlarm (
        var isSuccess: Boolean,
        var code : String,
        var message: String
            )
}