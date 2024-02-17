package com.example.aboutme.Alarm.api

class AlarmResponse {
    data class ResponseAlarm(
        var isSuccess : Boolean,
        var code : String,
        var result : AlarmResult
    )
    data class AlarmResult (
        var alarms : List<Alarms>
            )
    data class Alarms (
        var content : String,
        var profile_serial_number : Int,
        var space_id : Long
    )
}