package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val username: String,
    val password: String
)