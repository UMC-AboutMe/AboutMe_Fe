package com.example.aboutme

class LoginResponse {
    data class ResponseLogin(
        var isSuccess: Boolean,
        var code: String,
        var message: String,
        var result: LoginResult
    )
    data class LoginResult(
        var email: String,
        var jwtToken: String,
        var social: String
    )
    data class RequestLogin (
        var email : String
            )
}