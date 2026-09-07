package com.example.quizella.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizella.presentation.util.Screen
import com.example.quizella.ui.theme.MinimalPrimary
import com.example.quizella.ui.theme.MinimalSurfaceAlt
import com.example.quizella.ui.theme.NaturePrimary
import com.example.quizella.ui.theme.NatureSurfaceAlt
import com.example.quizella.ui.theme.ThemeMode

@Composable
fun MainScreen(
    navHostController: NavController,
    onLogout: () -> Unit,
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val points by viewModel.points

    var showThemeDialog by
    remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by
    remember {
        mutableStateOf(false)
    }

    Scaffold(

        backgroundColor =
            MaterialTheme.colors.background,

        topBar = {

            TopAppBar(

                backgroundColor =
                    MaterialTheme.colors.surface,

                elevation = 0.dp,

                title = {

                    Column {

                        Text(
                            text = "Quizella",
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Test your knowledge",

                            style =
                                MaterialTheme.typography.caption,

                            color =
                                MaterialTheme.colors.onSurface
                                    .copy(alpha = 0.55f)
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {
                            showThemeDialog = true
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Brightness6,

                            contentDescription =
                                "Change theme"
                        )
                    }

                    IconButton(
                        onClick = {
                            showLogoutDialog = true
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.ExitToApp,

                            contentDescription =
                                "Logout"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colors.background
                )
                .padding(paddingValues)
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // ==================================================
            // HEADER
            // ==================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Ready for a challenge?",

                        style =
                            MaterialTheme.typography.h5
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Your current best score",

                        style =
                            MaterialTheme.typography.body2,

                        color =
                            MaterialTheme.colors.onBackground
                                .copy(alpha = 0.6f)
                    )
                }

                Box(

                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colors.primary
                                .copy(alpha = 0.12f)
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            points.toString(),

                        style =
                            MaterialTheme.typography.h6,

                        color =
                            MaterialTheme.colors.primary,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // ==================================================
            // PROGRESS CARD
            // ==================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                backgroundColor =
                    MaterialTheme.colors.surface,

                shape =
                    MaterialTheme.shapes.large,

                elevation =
                    2.dp

            ) {

                Column(

                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(
                        text =
                            "Your progress",

                        style =
                            MaterialTheme.typography.h6
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        ProgressStat(
                            label = "High score",
                            value = points.toString()
                        )

                        ProgressStat(
                            label = "Questions",
                            value = "10"
                        )

                        ProgressStat(
                            label = "Theme",

                            value =
                                if (
                                    themeMode ==
                                    ThemeMode.NATURE_GREEN
                                ) {
                                    "Nature"
                                } else {
                                    "Light"
                                }
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            // ==================================================
            // START QUIZ
            // ==================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                backgroundColor =
                    MaterialTheme.colors.primary,

                shape =
                    MaterialTheme.shapes.large,

                elevation =
                    0.dp

            ) {

                Column(

                    modifier =
                        Modifier.padding(22.dp)
                ) {

                    Text(
                        text =
                            "Start a new quiz",

                        style =
                            MaterialTheme.typography.h6,

                        color =
                            MaterialTheme.colors.onPrimary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Challenge yourself and improve your score.",

                        style =
                            MaterialTheme.typography.body2,

                        color =
                            MaterialTheme.colors.onPrimary
                                .copy(alpha = 0.78f)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(18.dp)
                    )

                    Button(

                        onClick = {

                            navHostController.navigate(
                                Screen.QuizSetupScreen.route
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        colors =
                            ButtonDefaults.buttonColors(

                                backgroundColor =
                                    MaterialTheme.colors.surface,

                                contentColor =
                                    MaterialTheme.colors.primary
                            ),

                        shape =
                            MaterialTheme.shapes.medium

                    ) {

                        Text(
                            text =
                                "Start Quiz"
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            OutlinedButton(
                onClick = {
                    navHostController.navigate(Screen.ResultsScreen.route)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Quiz History")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // ==================================================
            // THEME BUTTON
            // ==================================================

            OutlinedButton(

                onClick = {
                    showThemeDialog = true
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Brightness6,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(

                    text =
                        if (
                            themeMode ==
                            ThemeMode.NATURE_GREEN
                        ) {

                            "Nature Green theme"

                        } else {

                            "Minimal Light theme"
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // ==================================================
            // LOGOUT
            // ==================================================

            OutlinedButton(

                onClick = {
                    showLogoutDialog = true
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.ExitToApp,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text("Logout")
            }
        }
    }

    // ==========================================================
    // THEME DIALOG
    // ==========================================================

    if (showThemeDialog) {

        ThemePickerDialog(

            selectedTheme =
                themeMode,

            onThemeSelected = {

                onThemeChange(it)

                showThemeDialog =
                    false
            },

            onDismiss = {
                showThemeDialog =
                    false
            }
        )
    }

    // ==========================================================
    // LOGOUT DIALOG
    // ==========================================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog =
                    false
            },

            title = {
                Text("Logout")
            },

            text = {
                Text(
                    "Are you sure you want to logout?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showLogoutDialog =
                            false

                        onLogout()
                    }

                ) {

                    Text("Logout")
                }
            },

            dismissButton = {

                OutlinedButton(

                    onClick = {
                        showLogoutDialog =
                            false
                    }

                ) {

                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProgressStat(
    label: String,
    value: String
) {

    Column {

        Text(
            text =
                value,

            style =
                MaterialTheme.typography.h6,

            color =
                MaterialTheme.colors.primary,

            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                label,

            style =
                MaterialTheme.typography.caption,

            color =
                MaterialTheme.colors.onSurface
                    .copy(alpha = 0.55f)
        )
    }
}

@Composable
private fun ThemePickerDialog(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Choose appearance",

                style =
                    MaterialTheme.typography.h6
            )
        },

        text = {

            Column {

                ThemeOption(

                    title =
                        "Minimal Light",

                    subtitle =
                        "Clean white and blue",

                    themeMode =
                        ThemeMode.MINIMAL_LIGHT,

                    selected =
                        selectedTheme ==
                                ThemeMode.MINIMAL_LIGHT,

                    accent =
                        MinimalPrimary,

                    preview =
                        MinimalSurfaceAlt,

                    onClick = {

                        onThemeSelected(
                            ThemeMode.MINIMAL_LIGHT
                        )
                    }
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                ThemeOption(

                    title =
                        "Nature Green",

                    subtitle =
                        "Warm cream and natural green",

                    themeMode =
                        ThemeMode.NATURE_GREEN,

                    selected =
                        selectedTheme ==
                                ThemeMode.NATURE_GREEN,

                    accent =
                        NaturePrimary,

                    preview =
                        NatureSurfaceAlt,

                    onClick = {

                        onThemeSelected(
                            ThemeMode.NATURE_GREEN
                        )
                    }
                )
            }
        },

        confirmButton = {}
    )
}

@Composable
private fun ThemeOption(

    title: String,

    subtitle: String,

    themeMode: ThemeMode,

    selected: Boolean,

    accent: Color,

    preview: Color,

    onClick: () -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clip(
                MaterialTheme.shapes.medium
            )
            .background(preview)
            .border(

                width =
                    if (selected) {
                        2.dp
                    } else {
                        1.dp
                    },

                color =
                    if (selected) {
                        accent
                    } else {
                        MaterialTheme.colors.onSurface
                            .copy(alpha = 0.12f)
                    },

                shape =
                    MaterialTheme.shapes.medium
            )
            .clickable(
                onClick = onClick
            )
            .padding(12.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(

            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(accent),

            contentAlignment =
                Alignment.Center
        ) {

            Text(

                text =
                    if (
                        themeMode ==
                        ThemeMode.MINIMAL_LIGHT
                    ) {
                        "M"
                    } else {
                        "N"
                    },

                color =
                    Color.White,

                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text =
                    title,

                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text =
                    subtitle,

                style =
                    MaterialTheme.typography.caption,

                color =
                    MaterialTheme.colors.onSurface
                        .copy(alpha = 0.6f)
            )
        }

        RadioButton(

            selected =
                selected,

            onClick =
                onClick,

            colors =
                RadioButtonDefaults.colors(
                    selectedColor =
                        accent
                )
        )
    }
}