package com.example.quizella.data.repository

import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.model.Category
import com.example.quizella.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val triviaService: TriviaService
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> =
        triviaService.getCategories().map {
            Category(
                id = it.id,
                name = it.name
            )
        }
}
