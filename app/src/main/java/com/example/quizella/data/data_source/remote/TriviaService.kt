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

        } catch (e: RedirectResponseException) {

            Log.e(
                "TriviaService",
                "Register redirect error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                "Registration failed. Please try again."
            )

        } catch (e: ClientRequestException) {

            Log.e(
                "TriviaService",
                "Register HTTP error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                getUserFriendlyError(
                    statusCode = e.response.status.value,
                    operation = "register"
                )
            )

        } catch (e: ServerResponseException) {

            Log.e(
                "TriviaService",
                "Register server error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                "Something went wrong on the server. Please try again later."
            )

        } catch (e: Exception) {

            Log.e(
                "TriviaService",
                "Register error: ${e.message}",
                e
            )

            throw IllegalStateException(
                "Unable to connect to the server. Please check your connection."
            )
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

        } catch (e: RedirectResponseException) {

            Log.e(
                "TriviaService",
                "Login redirect error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                "Login failed. Please try again."
            )

        } catch (e: ClientRequestException) {

            Log.e(
                "TriviaService",
                "Login HTTP error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                getUserFriendlyError(
                    statusCode = e.response.status.value,
                    operation = "login"
                )
            )

        } catch (e: ServerResponseException) {

            Log.e(
                "TriviaService",
                "Login server error: ${e.response.status.value} ${e.response.status.description}",
                e
            )

            throw IllegalStateException(
                "Something went wrong on the server. Please try again later."
            )

        } catch (e: Exception) {

            Log.e(
                "TriviaService",
                "Login error: ${e.message}",
                e
            )

            throw IllegalStateException(
                "Unable to connect to the server. Please check your connection."
            )
        }
    }

    private fun getUserFriendlyError(
        statusCode: Int,
        operation: String
    ): String {

        return when (statusCode) {

            400 -> {
                "The information you entered is not valid."
            }

            401 -> {
                if (operation == "login") {
                    "Incorrect username or password."
                } else {
                    "Authentication failed. Please try again."
                }
            }

            403 -> {
                "You don't have permission to perform this action."
            }

            404 -> {
                "Authentication service is unavailable."
            }

            409 -> {
                if (operation == "register") {
                    "This username is already taken."
                } else {
                    "This account already exists."
                }
            }

            422 -> {
                "Please check the information you entered."
            }

            429 -> {
                "Too many attempts. Please try again later."
            }

            500, 502, 503, 504 -> {
                "Something went wrong on the server. Please try again later."
            }

            else -> {
                "Something went wrong. Please try again."
            }
        }
    }
}