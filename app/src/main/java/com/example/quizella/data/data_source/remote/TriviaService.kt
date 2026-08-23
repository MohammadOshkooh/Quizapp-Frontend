package com.example.quizella.data.data_source.remote

import android.util.Log
import com.example.quizella.common.Constants
import com.example.quizella.data.data_source.remote.dto.AuthResponse
import com.example.quizella.data.data_source.remote.dto.LoginResponse
import com.example.quizella.data.data_source.remote.dto.QuestionResponse
import com.example.quizella.data.data_source.remote.dto.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

class TriviaService(
    private val client: HttpClient
) {

    suspend fun getQuestions(): List<QuestionResponse> {
        return try {

            client.get {
                url(Constants.QUESTIONS_URL)
            }

        } catch (e: RedirectResponseException) {

            Log.e(
                "TriviaService",
                "Redirect: ${e.response.status.description}",
                e
            )

            emptyList()

        } catch (e: ClientRequestException) {

            Log.e(
                "TriviaService",
                "Client error: ${e.response.status.description}",
                e
            )

            emptyList()

        } catch (e: ServerResponseException) {

            Log.e(
                "TriviaService",
                "Server error: ${e.response.status.description}",
                e
            )

            emptyList()

        } catch (e: Exception) {

            Log.e(
                "TriviaService",
                "Error: ${e.message}",
                e
            )

            emptyList()
        }
    }

    suspend fun register(
        username: String,
        password: String
    ): RegisterResponse {

        val request = AuthResponse(
            username = username,
            password = password
        )

        Log.d(
            "TriviaService",
            "Register username: $username"
        )

        return try {

            client.post {

                url(
                    Constants.REGISTER_URL
                )

                header(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                )

                body = request
            }

        } catch (e: ClientRequestException) {

            Log.e(
                "TriviaService",
                "Register HTTP error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw e

        } catch (e: ServerResponseException) {

            Log.e(
                "TriviaService",
                "Register server error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw e

        } catch (e: Exception) {

            Log.e(
                "TriviaService",
                "Register error: ${e.message}",
                e
            )

            throw e
        }
    }

    suspend fun login(
        username: String,
        password: String
    ): LoginResponse {

        val request = AuthResponse(
            username = username,
            password = password
        )

        Log.d(
            "TriviaService",
            "Login username: $username"
        )

        return try {

            client.post {

                url(
                    Constants.LOGIN_URL
                )

                header(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                )

                body = request
            }

        } catch (e: ClientRequestException) {

            Log.e(
                "TriviaService",
                "Login HTTP error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw e

        } catch (e: ServerResponseException) {

            Log.e(
                "TriviaService",
                "Login server error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw e

        } catch (e: Exception) {

            Log.e(
                "TriviaService",
                "Login error: ${e.message}",
                e
            )

            throw e
        }
    }
}