package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultRequest(
    val category: String,
    val question_types: List<String>,
    val question_count: Int,
    val correct_count: Int
)
