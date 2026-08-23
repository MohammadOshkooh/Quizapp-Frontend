package com.example.quizella.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.domain.model.User
import com.example.quizella.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(
    name = "auth_prefs"
)

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val triviaService: TriviaService
) : AuthRepository {

    companion object {

        private val IS_LOGGED_IN =
            booleanPreferencesKey("is_logged_in")

        private val USERNAME =
            stringPreferencesKey("username")

        private val ACCESS_TOKEN =
            stringPreferencesKey("access_token")
    }

    override suspend fun register(
        username: String,
        password: String
    ): Result<User> {

        return try {

            val response = triviaService.register(
                username = username,
                password = password
            )

            val user = User(
                id = response.id,
                username = response.username,
                passwordHash = ""
            )

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Result<User> {

        return try {

            val response = triviaService.login(
                username = username,
                password = password
            )

            saveLoginState(
                isLoggedIn = true,
                username = username,
                accessToken = response.access_token
            )

            val user = User(
                id = 0,
                username = username,
                passwordHash = ""
            )

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun saveLoginState(
        isLoggedIn: Boolean,
        username: String,
        accessToken: String
    ) {

        context.dataStore.edit { prefs ->

            prefs[IS_LOGGED_IN] = isLoggedIn

            if (isLoggedIn) {

                prefs[USERNAME] = username

                if (accessToken.isNotEmpty()) {
                    prefs[ACCESS_TOKEN] = accessToken
                }

            } else {

                prefs.remove(USERNAME)
                prefs.remove(ACCESS_TOKEN)
            }
        }
    }

    override fun getLoginState(): Flow<Pair<Boolean, String>> {

        return context.dataStore.data.map { prefs ->

            val isLoggedIn =
                prefs[IS_LOGGED_IN] ?: false

            val username =
                prefs[USERNAME] ?: ""

            isLoggedIn to username
        }
    }

    override suspend fun getAccessToken(): String? {

        return context.dataStore.data
            .map { prefs ->
                prefs[ACCESS_TOKEN]
            }
            .first()
    }

    override suspend fun logout() {

        saveLoginState(
            isLoggedIn = false,
            username = "",
            accessToken = ""
        )
    }
}