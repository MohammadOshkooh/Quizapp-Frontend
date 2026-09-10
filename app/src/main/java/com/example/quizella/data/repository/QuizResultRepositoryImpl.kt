package com.example.quizella.data.repository

import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.data.data_source.remote.dto.QuizResultRequest
import com.example.quizella.domain.model.QuizResult
import com.example.quizella.domain.repository.AuthRepository
import com.example.quizella.domain.repository.QuizResultRepository
import javax.inject.Inject

class QuizResultRepositoryImpl @Inject constructor(
    private val triviaService: TriviaService,
    private val authRepository: AuthRepository
) : QuizResultRepository {

    override suspend fun saveQuizResult(
        quizToken: String,
        historyIds: List<Int>
    ): Result<QuizResult> {
        return try {

            val token = authRepository.getAccessToken()
                ?: return Result.failure(
                    IllegalStateException(
                        "Authentication token is missing."
                    )
                )

            val response = triviaService.saveQuizResult(
                token = token,
                request = QuizResultRequest(
                    quiz_token = quizToken,
                    history_ids = historyIds
                )
            )

            Result.success(
                response.toDomain()
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun getQuizResults():
            Result<List<QuizResult>> {

        return try {

            val token = authRepository.getAccessToken()
                ?: return Result.failure(
                    IllegalStateException(
                        "Authentication token is missing."
                    )
                )

            val response =
                triviaService.getQuizResults(token)

            Result.success(
                response.map {
                    it.toDomain()
                }
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    private fun com.example.quizella.data.data_source.remote.dto.QuizResultResponse.toDomain() =
        QuizResult(
            id = id,
            category = category,
            questionTypes = question_types,
            questionCount = question_count,
            correctCount = correct_count,
            score = score,
            completedAt = completed_at
        )
}