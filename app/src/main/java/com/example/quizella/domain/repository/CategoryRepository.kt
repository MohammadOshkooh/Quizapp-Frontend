package com.example.quizella.domain.repository

import com.example.quizella.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
