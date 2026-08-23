package com.example.quizella.presentation.util

/**
 * Application navigation destinations.
 * Defines all possible screens within the app.
 *
 * @property route The unique string identifier for each screen.
 */
sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object MainScreen : Screen("main_screen")
    object QuestionScreen : Screen("question_screen")
    object FinishScreen : Screen("finish_screen")
}