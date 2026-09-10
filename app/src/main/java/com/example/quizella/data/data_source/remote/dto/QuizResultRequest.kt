package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultRequest(
    val quiz_token: String,
    val history_ids: List<Int>
)