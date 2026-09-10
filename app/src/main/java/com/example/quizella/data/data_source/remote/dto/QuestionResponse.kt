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

    // Answer mechanism.
    // Every Quizella question is multiple choice.
    val type: String = "multiple_choice",

    // Actual content/media type.
    // text / audio / image
    val mediaType: String = "text",

    val difficulty: String,

    val regions: List<String>? = null,

    val isNiche: Boolean = false,

    // Relative or absolute backend media URL.
    val mediaUrl: String? = null
)