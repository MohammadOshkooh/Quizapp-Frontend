package com.example.quizella.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizella.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow(AuthState())

    val state: StateFlow<AuthState> =
        _state.asStateFlow()

    init {

        restoreAuthenticationState()
    }

    private fun restoreAuthenticationState() {

        viewModelScope.launch {

            authRepository
                .getLoginState()
                .collect { (isLoggedIn, username) ->

                    _state.update {

                        it.copy(
                            isLoggedIn = isLoggedIn,
                            username = if (isLoggedIn) {
                                username
                            } else {
                                ""
                            }
                        )
                    }
                }
        }
    }

    fun onEvent(
        event: AuthEvent
    ) {

        when (event) {

            is AuthEvent.OnUsernameChange -> {

                _state.update {

                    it.copy(
                        username = event.username,
                        error = null
                    )
                }
            }

            is AuthEvent.OnPasswordChange -> {

                _state.update {

                    it.copy(
                        password = event.password,
                        error = null
                    )
                }
            }

            is AuthEvent.OnConfirmPasswordChange -> {

                _state.update {

                    it.copy(
                        confirmPassword =
                            event.confirmPassword,
                        error = null
                    )
                }
            }

            is AuthEvent.OnLogin -> {

                login(
                    event.username,
                    event.password
                )
            }

            is AuthEvent.OnRegister -> {

                register(
                    event.username,
                    event.password,
                    event.confirmPassword
                )
            }

            is AuthEvent.ClearError -> {

                _state.update {

                    it.copy(
                        error = null
                    )
                }
            }
        }
    }

    private fun login(
        username: String,
        password: String
    ) {

        if (
            username.isBlank() ||
            password.isBlank()
        ) {

            _state.update {

                it.copy(
                    error =
                        "Username and password cannot be empty"
                )
            }

            return
        }

        viewModelScope.launch {

            _state.update {

                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val result =
                authRepository.login(
                    username,
                    password
                )

            result
                .onSuccess { user ->

                    _state.update {

                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            username = user.username,
                            password = "",
                            confirmPassword = "",
                            error = null
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {

                        it.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error =
                                error.message
                                    ?: "Login failed"
                        )
                    }
                }
        }
    }

    private fun register(
        username: String,
        password: String,
        confirmPassword: String
    ) {

        if (username.isBlank()) {

            _state.update {

                it.copy(
                    error = "Username cannot be empty"
                )
            }

            return
        }

        if (password.isBlank()) {

            _state.update {

                it.copy(
                    error = "Password cannot be empty"
                )
            }

            return
        }

        if (password != confirmPassword) {

            _state.update {

                it.copy(
                    error = "Passwords do not match"
                )
            }

            return
        }

        viewModelScope.launch {

            _state.update {

                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val result =
                authRepository.register(
                    username,
                    password
                )

            result
                .onSuccess {

                    /*
                     * Registration is followed by login
                     * so the newly registered user receives
                     * and stores a JWT session.
                     */
                    login(
                        username,
                        password
                    )
                }
                .onFailure { error ->

                    _state.update {

                        it.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error =
                                error.message
                                    ?: "Registration failed"
                        )
                    }
                }
        }
    }

    fun logout() {

        viewModelScope.launch {

            authRepository.logout()

            _state.update {

                it.copy(
                    isLoggedIn = false,
                    username = "",
                    password = "",
                    confirmPassword = "",
                    error = null
                )
            }
        }
    }
}