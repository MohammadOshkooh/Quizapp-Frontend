package com.example.quizella.domain.repository

import com.example.quizella.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication.
 *
 * User accounts are stored on the backend.
 * DataStore is only used to keep the local JWT session.
 */
interface AuthRepository {

    /**
     * Registers a new user through the backend API.
     */
    suspend fun register(
        username: String,
        password: String
    ): Result<User>

    /**
     * Logs in through the backend API.
     */
    suspend fun login(
        username: String,
        password: String
    ): Result<User>

    /**
     * Saves the local authentication session.
     */
    suspend fun saveLoginState(
        isLoggedIn: Boolean,
        username: String = "",
        accessToken: String = ""
    )

    /**
     * Observes the current local authentication state.
     */
    fun getLoginState(): Flow<Pair<Boolean, String>>

    /**
     * Returns the stored JWT.
     */
    suspend fun getAccessToken(): String?

    /**
     * Clears the local session.
     */
    suspend fun logout()
}