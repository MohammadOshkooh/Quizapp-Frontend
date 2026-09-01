package com.example.quizella.domain.model

data class QuizConfig(
    val category: String,
    val questionTypes: List<String>,
    val questionCount: Int
)