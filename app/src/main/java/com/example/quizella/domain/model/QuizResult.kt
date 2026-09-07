package com.example.quizella.domain.model

data class QuizResult(
    val id: Int,
    val category: String,
    val questionTypes: List<String>,
    val questionCount: Int,
    val correctCount: Int,
    val score: Int,
    val completedAt: String
)
