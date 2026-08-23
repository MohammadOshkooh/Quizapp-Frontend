package com.example.quizella.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizella.presentation.auth.AuthViewModel
import com.example.quizella.presentation.auth.LoginScreen
import com.example.quizella.presentation.auth.RegisterScreen
import com.example.quizella.presentation.finish.FinishScreen
import com.example.quizella.presentation.main.MainScreen
import com.example.quizella.presentation.questions.QuestionScreen
import com.example.quizella.presentation.util.Screen
import com.example.quizella.ui.theme.QuizellaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            QuizellaTheme {

                val authViewModel: AuthViewModel =
                    hiltViewModel()

                val authState by
                authViewModel.state.collectAsState()

                /*
                 * Do not create the navigation graph until
                 * DataStore has restored the authentication state.
                 *
                 * null means:
                 * "we don't know yet whether the user is logged in."
                 */
                if (authState.isLoggedIn == null) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }

                } else {

                    val navController =
                        rememberNavController()

                    NavHost(
                        navController = navController,

                        startDestination =
                            if (authState.isLoggedIn == true) {
                                Screen.MainScreen.route
                            } else {
                                Screen.LoginScreen.route
                            }
                    ) {

                        composable(
                            Screen.LoginScreen.route
                        ) {

                            LoginScreen(
                                navController = navController,
                                onLoginSuccess = {

                                    navController.navigate(
                                        Screen.MainScreen.route
                                    ) {

                                        popUpTo(
                                            Screen.LoginScreen.route
                                        ) {

                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(
                            Screen.RegisterScreen.route
                        ) {

                            RegisterScreen(
                                navController = navController,
                                onRegisterSuccess = {

                                    navController.navigate(
                                        Screen.MainScreen.route
                                    ) {

                                        popUpTo(
                                            Screen.RegisterScreen.route
                                        ) {

                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(
                            Screen.MainScreen.route
                        ) {

                            MainScreen(
                                navHostController =
                                    navController,

                                onLogout = {

                                    authViewModel.logout()

                                    navController.navigate(
                                        Screen.LoginScreen.route
                                    ) {

                                        popUpTo(
                                            Screen.MainScreen.route
                                        ) {

                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(
                            Screen.QuestionScreen.route
                        ) {

                            QuestionScreen(
                                navHostController =
                                    navController
                            )
                        }

                        composable(
                            Screen.FinishScreen.route
                        ) {

                            FinishScreen(
                                navHostController =
                                    navController
                            )
                        }
                    }
                }
            }
        }
    }
}