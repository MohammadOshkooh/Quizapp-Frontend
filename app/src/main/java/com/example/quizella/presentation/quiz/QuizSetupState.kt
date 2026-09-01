package com.example.quizella.presentation.quiz

import com.example.quizella.domain.model.Category

data class QuizSetupState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedTypes: Set<String> = setOf("text"),
    val selectedCount: Int = 10,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)