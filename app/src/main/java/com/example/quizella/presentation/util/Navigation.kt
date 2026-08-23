package com.example.quizella.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizella.presentation.finish.FinishScreen
import com.example.quizella.presentation.main.MainScreen
import com.example.quizella.presentation.questions.QuestionScreen
import com.example.quizella.ui.theme.ThemeMode

/**
 * Sets up the navigation graph for the application.
 *
 * @param navController The NavHostController instance.
 * @param onLogout Callback for logout action.
 * @param themeMode Current UI theme mode.
 * @param onThemeChange Callback for changing the UI theme.
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

        // ==================================================
        // MAIN
        // ==================================================

        composable(
            Screen.MainScreen.route
        ) {

            MainScreen(
                navHostController = navController,
                onLogout = onLogout,
                themeMode = themeMode,
                onThemeChange = onThemeChange
            )
        }

        // ==================================================
        // QUESTIONS
        // ==================================================

        composable(
            Screen.QuestionScreen.route
        ) {

            QuestionScreen(
                navHostController = navController
            )
        }

        // ==================================================
        // FINISH
        // ==================================================

        composable(
            Screen.FinishScreen.route
        ) {

            FinishScreen(
                navHostController = navController
            )
        }
    }
}