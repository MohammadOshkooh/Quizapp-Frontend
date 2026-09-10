package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnswerResponse(
    val correct: Boolean,
    val correct_answer: String,
    val history_id: Int
)