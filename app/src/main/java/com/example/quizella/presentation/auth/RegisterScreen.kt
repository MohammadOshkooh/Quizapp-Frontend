package com.example.quizella.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun RegisterScreen(

    navController: NavController,

    onRegisterSuccess: () -> Unit,

    viewModel: AuthViewModel =
        hiltViewModel()

) {

    val state by
    viewModel.state.collectAsState()

    LaunchedEffect(
        state.isLoggedIn
    ) {

        if (
            state.isLoggedIn == true
        ) {

            onRegisterSuccess()
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
                    "Create your account",

                style =
                    MaterialTheme.typography.h4
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(

                text =
                    "Join Quizella and start testing your knowledge.",

                style =
                    MaterialTheme.typography.body2,

                color =
                    MaterialTheme.colors.onSurface
                        .copy(alpha = 0.6f)
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
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
                    Modifier.height(12.dp)
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

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedTextField(

                value =
                    state.confirmPassword,

                onValueChange = {

                    viewModel.onEvent(
                        AuthEvent.OnConfirmPasswordChange(it)
                    )
                },

                label = {
                    Text("Confirm password")
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
                                    "match",
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

                        AuthEvent.OnRegister(

                            state.username,

                            state.password,

                            state.confirmPassword
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
                            Modifier.size(20.dp),

                        color =
                            MaterialTheme.colors.onPrimary,

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        "Create account"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            TextButton(

                onClick = {

                    navController.navigate(
                        Screen.LoginScreen.route
                    ) {

                        popUpTo(
                            Screen.RegisterScreen.route
                        ) {

                            inclusive =
                                true
                        }
                    }
                },

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(
                    "Already have an account?  Login"
                )
            }
        }
    }
}