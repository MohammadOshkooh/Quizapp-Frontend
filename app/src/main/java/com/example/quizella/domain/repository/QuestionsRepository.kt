package com.example.quizella.domain.repository

import com.example.quizella.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {

    suspend fun getQuestions(
        category: String,
        questionTypes: List<String>,
        limit: Int
    ): Flow<List<Question>>

    suspend fun submitAnswer(
        questionId: Int,
        selectedAnswer: String,
        quizToken: String
    ): Result<Pair<String, Int>>

    suspend fun clearQuestions()
}