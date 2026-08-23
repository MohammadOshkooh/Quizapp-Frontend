package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String? = null,
    val created_at: String
)