package com.example.quizella.domain.repository

import com.example.quizella.domain.model.QuizResult

interface QuizResultRepository {

    suspend fun saveQuizResult(
        quizToken: String,
        historyIds: List<Int>
    ): Result<QuizResult>

    suspend fun getQuizResults():
            Result<List<QuizResult>>
}