package com.example.quizella.presentation.auth

data class AuthState(
    val isLoading: Boolean = false,

    val isLoggedIn: Boolean? = null,

    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isRegisterMode: Boolean = false
)