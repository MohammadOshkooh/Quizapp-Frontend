package com.example.quizella.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizella.presentation.finish.FinishScreen
import com.example.quizella.presentation.main.MainScreen
import com.example.quizella.presentation.questions.QuestionScreen
import com.example.quizella.presentation.quiz.QuizSetupScreen
import com.example.quizella.ui.theme.ThemeMode

/**
 * Optional reusable navigation graph kept in sync with MainActivity's graph.
 * MainActivity remains the active entry point for the application.
 */
@Composable
fun SetupNavigation(
    navController: NavHostController,
    onLogout: () -> Unit,
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(
                navHostController = navController,
                onLogout = onLogout,
                themeMode = themeMode,
                onThemeChange = onThemeChange
            )
        }

        composable(Screen.QuizSetupScreen.route) {
            QuizSetupScreen(navHostController = navController)
        }

        composable(
            route = Screen.QuestionScreen.routePattern,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
                navArgument("count") { type = NavType.StringType }
            )
        ) {
            QuestionScreen(navHostController = navController)
        }

        composable(Screen.FinishScreen.route) {
            FinishScreen(navHostController = navController)
        }
    }
}
