package com.example.quizella.presentation.finish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.padding

@Composable
fun FinishScreen(

    viewModel:
    FinishViewModel =
        hiltViewModel(),

    navHostController:
    NavHostController

) {

    val points =
        viewModel.points.value

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(

            text =
                "Quiz complete!",

            style =
                MaterialTheme.typography.h4,

            color =
                MaterialTheme.colors.onBackground
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(

            text =
                "Great job. Here is your final score.",

            style =
                MaterialTheme.typography.body2,

            color =
                MaterialTheme.colors.onBackground
                    .copy(alpha = 0.6f)
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

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
                    Modifier.padding(28.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(

                    text =
                        "YOUR SCORE",

                    style =
                        MaterialTheme.typography.caption,

                    color =
                        MaterialTheme.colors.primary,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(

                    text =
                        points.toString(),

                    fontSize =
                        72.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme.colors.onSurface
                )

                Text(

                    text =
                        "points",

                    style =
                        MaterialTheme.typography.body2,

                    color =
                        MaterialTheme.colors.onSurface
                            .copy(alpha = 0.55f)
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(

            onClick = {

                viewModel.onEvent(

                    FinishEvent.PlayAgain(
                        navHostController
                    )
                )
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),

            shape =
                MaterialTheme.shapes.medium

        ) {

            Icon(

                imageVector =
                    Icons.Outlined.Sync,

                contentDescription =
                    null
            )

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Text("Play again")
        }

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)

        ) {

            OutlinedButton(

                onClick = {

                    viewModel.onEvent(

                        FinishEvent.Share(

                            context =
                                navHostController.context
                        )
                    )
                },

                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Icon(

                    imageVector =
                        Icons.Outlined.Share,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text("Share")
            }

            OutlinedButton(

                onClick = {

                    viewModel.onEvent(

                        FinishEvent.GoHome(
                            navHostController
                        )
                    )
                },

                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Icon(

                    imageVector =
                        Icons.Outlined.Home,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text("Home")
            }
        }
    }
}