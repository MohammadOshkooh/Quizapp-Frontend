package com.example.quizella.data.repository

import com.example.quizella.data.data_source.local.QuestionCacheDao
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.model.Question
import com.example.quizella.domain.repository.QuestionsRepository
import kotlinx.coroutines.flow.Flow

class QuestionsRepositoryImpl(
    private val dao: QuestionCacheDao,
    private val triviaService: TriviaService
) : QuestionsRepository {

    override suspend fun getQuestions(
        category: String,
        questionTypes: List<String>,
        limit: Int
    ): Flow<List<Question>> {

        dao.clear()

        triviaService.getQuestions(
            category = category,
            questionTypes = questionTypes,
            limit = limit
        ).forEach { question ->

            dao.insertQuestion(
                Question(
                    id = question.id,

                    category = question.category,

                    correctAnswer =
                        question.correctAnswer,

                    answers = (
                            question.incorrectAnswers
                                    + question.correctAnswer
                            ).shuffled(),

                    question = question.question,

                    tags = question.tags,

                    // Always multiple choice.
                    type = question.type,

                    // Actual media type.
                    mediaType = question.mediaType,

                    difficulty = question.difficulty,

                    regions = question.regions,

                    isNiche = question.isNiche,

                    mediaUrl = question.mediaUrl
                )
            )
        }

        return dao.getQuestions()
    }

    override suspend fun clearQuestions() {
        dao.clear()
    }
}