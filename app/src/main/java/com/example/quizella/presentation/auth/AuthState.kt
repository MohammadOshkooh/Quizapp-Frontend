package com.example.quizella.presentation.auth

data class AuthState(
    val isLoading: Boolean = false,

    /*
     * null = authentication state has not been restored yet.
     * true = user is logged in.
     * false = user is logged out.
     */
    val isLoggedIn: Boolean? = null,

    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isRegisterMode: Boolean = false
)