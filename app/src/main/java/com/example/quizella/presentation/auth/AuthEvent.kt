package com.example.quizella.presentation.auth

sealed class AuthEvent {
    data class OnUsernameChange(val username: String) : AuthEvent()
    data class OnPasswordChange(val password: String) : AuthEvent()
    data class OnConfirmPasswordChange(val confirmPassword: String) : AuthEvent()
    data class OnLogin(val username: String, val password: String) : AuthEvent()
    data class OnRegister(
        val username: String,
        val password: String,
        val confirmPassword: String
    ) : AuthEvent()
    object ClearError : AuthEvent()
}