package com.example.quizella.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.quizella.presentation.theme.ThemeViewModel
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

            val authViewModel: AuthViewModel =
                hiltViewModel()

            val themeViewModel: ThemeViewModel =
                hiltViewModel()

            val authState by
            authViewModel.state.collectAsState()

            val themeMode by
            themeViewModel.theme.collectAsState()

            QuizellaTheme(
                themeMode = themeMode
            ) {

                val navController =
                    rememberNavController()

                /*
                 * Authentication state:
                 *
                 * null  -> DataStore is still restoring the state.
                 * true  -> User is logged in.
                 * false -> User is logged out.
                 *
                 * We intentionally do NOT create the NavHost while
                 * the authentication state is null.
                 *
                 * This prevents the LoginScreen from flashing for
                 * a moment when an already logged-in user opens the app.
                 */

                if (authState.isLoggedIn != null) {

                    val startDestination =
                        if (authState.isLoggedIn == true) {
                            Screen.MainScreen.route
                        } else {
                            Screen.LoginScreen.route
                        }

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {

                        // ==================================================
                        // LOGIN
                        // ==================================================

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

                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // ==================================================
                        // REGISTER
                        // ==================================================

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

                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // ==================================================
                        // MAIN
                        // ==================================================

                        composable(
                            Screen.MainScreen.route
                        ) {

                            if (authState.isLoggedIn == true) {

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

                                            launchSingleTop = true
                                        }
                                    },

                                    themeMode = themeMode,

                                    onThemeChange =
                                        themeViewModel::setTheme
                                )
                            }
                        }

                        // ==================================================
                        // QUESTIONS
                        // ==================================================

                        composable(
                            Screen.QuestionScreen.route
                        ) {

                            if (authState.isLoggedIn == true) {

                                QuestionScreen(
                                    navHostController =
                                        navController
                                )

                            } else {

                                navController.navigate(
                                    Screen.LoginScreen.route
                                ) {

                                    popUpTo(
                                        Screen.QuestionScreen.route
                                    ) {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        }

                        // ==================================================
                        // FINISH
                        // ==================================================

                        composable(
                            Screen.FinishScreen.route
                        ) {

                            if (authState.isLoggedIn == true) {

                                FinishScreen(
                                    navHostController =
                                        navController
                                )

                            } else {

                                navController.navigate(
                                    Screen.LoginScreen.route
                                ) {

                                    popUpTo(
                                        Screen.FinishScreen.route
                                    ) {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}