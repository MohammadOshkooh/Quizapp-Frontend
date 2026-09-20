package com.example.quizella.domain.repository

import com.example.quizella.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(
        username: String,
        password: String
    ): Result<User>

    suspend fun login(
        username: String,
        password: String
    ): Result<User>

    suspend fun saveLoginState(
        isLoggedIn: Boolean,
        username: String = "",
        accessToken: String = ""
    )

    fun getLoginState(): Flow<Pair<Boolean, String>>

    suspend fun getAccessToken(): String?

    suspend fun logout()
}