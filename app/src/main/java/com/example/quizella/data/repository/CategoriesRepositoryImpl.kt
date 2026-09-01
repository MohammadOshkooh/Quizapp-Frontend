package com.example.quizella.data.repository

import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.model.Category
import com.example.quizella.domain.repository.CategoriesRepository

class CategoriesRepositoryImpl(
    private val triviaService: TriviaService
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> {
        return triviaService.getCategories().map {
            Category(
                id = it.id,
                name = it.name
            )
        }
    }
}
