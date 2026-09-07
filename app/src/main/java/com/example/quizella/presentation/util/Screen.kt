package com.example.quizella.presentation.util

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object MainScreen : Screen("main_screen")
    object QuizSetupScreen : Screen("quiz_setup_screen")
    object QuestionScreen : Screen("question_screen") {
        const val routePattern = "question_screen?category={category}&type={type}&count={count}"

        fun createRoute(category: String, type: String, count: Int): String =
            "question_screen?category=$category&type=$type&count=$count"
    }
    object FinishScreen : Screen("finish_screen")
    object ResultsScreen : Screen("results_screen")
}
