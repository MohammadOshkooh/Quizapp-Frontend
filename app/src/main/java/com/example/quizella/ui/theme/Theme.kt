package com.example.quizella.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MinimalLightColors = lightColors(

    primary = MinimalPrimary,

    primaryVariant = MinimalPrimaryDark,

    secondary = MinimalSecondary,

    background = MinimalBackground,

    surface = MinimalSurface,

    onPrimary = Color.White,

    onSecondary = Color.White,

    onBackground = MinimalText,

    onSurface = MinimalText,

    error = QuizError,

    onError = Color.White
)

private val NatureGreenColors = lightColors(

    primary = NaturePrimary,

    primaryVariant = NaturePrimaryDark,

    secondary = NatureSecondary,

    background = NatureBackground,

    surface = NatureSurface,

    onPrimary = Color.White,

    onSecondary = NatureText,

    onBackground = NatureText,

    onSurface = NatureText,

    error = QuizError,

    onError = Color.White
)

@Composable
fun QuizellaTheme(
    themeMode: ThemeMode = ThemeMode.MINIMAL_LIGHT,
    content: @Composable () -> Unit
) {

    val colors = when (themeMode) {

        ThemeMode.MINIMAL_LIGHT ->
            MinimalLightColors

        ThemeMode.NATURE_GREEN ->
            NatureGreenColors
    }

    MaterialTheme(

        colors = colors,

        typography = Typography,

        shapes = Shapes,

        content = content
    )
}