package com.example.quizella.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val category: String,
    val id: String,
    val answers: List<String>,
    val quizToken: String,
    val tags: List<String> = emptyList(),
    val question: String,


    val type: String = "multiple_choice",


    val mediaType: String = "text",

    val difficulty: String,

    val regions: List<String>? = null,

    val isNiche: Boolean = false,

    val mediaUrl: String? = null
)