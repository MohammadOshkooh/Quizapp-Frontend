package com.example.quizella.domain.repository

import com.example.quizella.domain.model.QuizResult

interface QuizResultRepository {
    suspend fun saveQuizResult(
        category: String,
        questionTypes: List<String>,
        questionCount: Int,
        correctCount: Int
    ): Result<QuizResult>

    suspend fun getQuizResults(): Result<List<QuizResult>>
}
