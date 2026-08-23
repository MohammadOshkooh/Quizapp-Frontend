package com.example.quizella.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizella.presentation.util.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val state by
    viewModel.state.collectAsState()

    LaunchedEffect(
        state.isLoggedIn
    ) {

        if (
            state.isLoggedIn == true
        ) {

            onLoginSuccess()
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            )
            .padding(
                horizontal = 24.dp,
                vertical = 28.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    MaterialTheme.shapes.large
                )
                .background(
                    MaterialTheme.colors.surface
                )
                .padding(24.dp)
        ) {

            Text(

                text =
                    "Quizella",

                color =
                    MaterialTheme.colors.primary,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(

                text =
                    "Welcome back!",

                style =
                    MaterialTheme.typography.h4
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(

                text =
                    "Login to continue your quiz journey.",

                style =
                    MaterialTheme.typography.body2,

                color =
                    MaterialTheme.colors.onSurface
                        .copy(alpha = 0.6f)
            )

            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )

            OutlinedTextField(

                value =
                    state.username,

                onValueChange = {

                    viewModel.onEvent(
                        AuthEvent.OnUsernameChange(it)
                    )
                },

                label = {
                    Text("Username")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                isError =
                    state.error != null &&
                            state.error!!
                                .contains(
                                    "username",
                                    ignoreCase = true
                                )
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            OutlinedTextField(

                value =
                    state.password,

                onValueChange = {

                    viewModel.onEvent(
                        AuthEvent.OnPasswordChange(it)
                    )
                },

                label = {
                    Text("Password")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                visualTransformation =
                    PasswordVisualTransformation(),

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Password
                    ),

                isError =
                    state.error != null &&
                            state.error!!
                                .contains(
                                    "password",
                                    ignoreCase = true
                                )
            )

            if (
                state.error != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(

                    text =
                        state.error!!,

                    color =
                        MaterialTheme.colors.error,

                    style =
                        MaterialTheme.typography.body2
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(

                onClick = {

                    viewModel.onEvent(

                        AuthEvent.OnLogin(

                            state.username,

                            state.password
                        )
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                enabled =
                    !state.isLoading,

                shape =
                    MaterialTheme.shapes.medium,

                colors =
                    ButtonDefaults.buttonColors(

                        backgroundColor =
                            MaterialTheme.colors.primary,

                        contentColor =
                            MaterialTheme.colors.onPrimary
                    )
            ) {

                if (
                    state.isLoading
                ) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.height(20.dp),

                        color =
                            MaterialTheme.colors.onPrimary,

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text("Login")
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            TextButton(

                onClick = {

                    navController.navigate(
                        Screen.RegisterScreen.route
                    )
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Don't have an account?  Create one"
                )
            }
        }
    }
}