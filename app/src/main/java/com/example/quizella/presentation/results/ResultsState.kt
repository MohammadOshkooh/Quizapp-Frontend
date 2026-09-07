package com.example.quizella.presentation.results

import com.example.quizella.domain.model.QuizResult

data class ResultsState(
    val isLoading: Boolean = true,
    val results: List<QuizResult> = emptyList(),
    val errorMessage: String? = null
)
