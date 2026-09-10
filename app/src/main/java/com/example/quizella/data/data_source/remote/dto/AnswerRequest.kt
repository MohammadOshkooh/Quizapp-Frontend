package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnswerRequest(
    val question_id: Int,
    val selected_answer: String,
    val quiz_token: String
)