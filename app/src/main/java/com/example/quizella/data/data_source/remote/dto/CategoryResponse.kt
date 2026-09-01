package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: Int,
    val name: String
)
