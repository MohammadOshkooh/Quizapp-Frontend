package com.example.quizella.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizella.presentation.finish.FinishScreen
import com.example.quizella.presentation.main.MainScreen
import com.example.quizella.presentation.questions.QuestionScreen

/**
 * Sets up the navigation graph for the application.
 *
 * @param navController The NavHostController instance.
 * @param onLogout Callback for logout action.
 */
@Composable
fun SetupNavigation(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(
                navHostController = navController,
                onLogout = onLogout
            )
        }
        composable(Screen.QuestionScreen.route) {
            QuestionScreen(navHostController = navController)
        }
        composable(Screen.FinishScreen.route) {
            FinishScreen(navHostController = navController)
        }
    }
}