package com.example.quizella.data.repository

import com.example.quizella.data.data_source.local.QuestionCacheDao
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.model.Question
import com.example.quizella.domain.repository.AuthRepository
import com.example.quizella.domain.repository.QuestionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionsRepositoryImpl @Inject constructor(
    private val dao: QuestionCacheDao,
    private val triviaService: TriviaService,
    private val authRepository: AuthRepository
) : QuestionsRepository {

    override suspend fun getQuestions(
        category: String,
        questionTypes: List<String>,
        limit: Int
    ): Flow<List<Question>> {

        val token = authRepository.getAccessToken()
            ?: throw IllegalStateException(
                "Authentication token is missing."
            )

        dao.clear()

        triviaService.getQuestions(
            token = token,
            category = category,
            questionTypes = questionTypes,
            limit = limit
        ).forEach { question ->

            dao.insertQuestion(
                Question(
                    id = question.id,

                    category = question.category,

                    // Kept empty until the backend validates the answer.
                    correctAnswer = "",

                    answers = question.answers.shuffled(),

                    quizToken = question.quizToken,

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

    override suspend fun submitAnswer(
        questionId: Int,
        selectedAnswer: String,
        quizToken: String
    ): Result<Pair<String, Int>> {
        return try {

            val token = authRepository.getAccessToken()
                ?: return Result.failure(
                    IllegalStateException(
                        "Authentication token is missing."
                    )
                )

            val response = triviaService.answerQuestion(
                token = token,
                questionId = questionId,
                selectedAnswer = selectedAnswer,
                quizToken = quizToken
            )

            Result.success(
                response.correct_answer to response.history_id
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun clearQuestions() {
        dao.clear()
    }
}