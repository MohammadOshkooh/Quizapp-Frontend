package com.example.quizella.presentation.questions

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizella.R
import com.example.quizella.domain.model.Question
import com.example.quizella.ui.theme.QuizError
import com.example.quizella.ui.theme.QuizSuccess

@Composable
fun QuestionScreen(

    viewModel: QuestionViewModel =
        hiltViewModel(),

    navHostController:
    NavHostController

) {

    val currQuestion by
    viewModel.currQuestionState

    val context =
        LocalContext.current

    val activity =
        LocalContext.current as? Activity

    val dialogState by
    viewModel.dialogState

    BackHandler {

        viewModel.onEvent(
            QuestionEvent.OpenDialog
        )
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            )
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(

                    text =
                        "Quizella",

                    color =
                        MaterialTheme.colors.primary,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(

                    text =
                        "Question ${currQuestion.questionNo ?: 1}",

                    style =
                        MaterialTheme.typography.caption,

                    color =
                        MaterialTheme.colors.onBackground
                            .copy(alpha = 0.55f)
                )
            }

            IconButton(

                onClick = {

                    viewModel.onEvent(
                        QuestionEvent.OpenDialog
                    )
                }

            ) {

                Icon(

                    imageVector =
                        Icons.Outlined.Close,

                    contentDescription =
                        stringResource(
                            R.string.exit_app
                        ),

                    tint =
                        MaterialTheme.colors.onBackground
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        if (
            dialogState.state
        ) {

            AlertDialog(

                onDismissRequest = {

                    viewModel.onEvent(
                        QuestionEvent.CloseDialog
                    )
                },

                title = {
                    Text("Leave quiz?")
                },

                text = {
                    Text(
                        dialogState.text
                    )
                },

                confirmButton = {

                    Button(

                        onClick = {
                            activity?.finish()
                        }

                    ) {

                        Text("Yes")
                    }
                },

                dismissButton = {

                    TextButton(

                        onClick = {

                            viewModel.onEvent(
                                QuestionEvent.CloseDialog
                            )
                        }

                    ) {

                        Text("Stay")
                    }
                }
            )
        }

        val currentQuestion =
            currQuestion.currQuestion

        if (

            currentQuestion != null &&

            currentQuestion.question.isNotEmpty()

        ) {

            QuestionCard(

                questionNo =
                    currQuestion.questionNo ?: 1,

                question =
                    currentQuestion,

                isEnabled =
                    currQuestion.isEnabled,

                currAns =
                    currQuestion.currAns,

                onClick = {

                    viewModel.onEvent(
                        QuestionEvent.ChangeAnswer(it)
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Button(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                onClick = {

                    if (
                        currQuestion.currAns.isNotEmpty()
                    ) {

                        viewModel.onEvent(

                            QuestionEvent.NextQuestion(
                                navHostController
                            )
                        )

                    } else {

                        Toast.makeText(

                            context,

                            context.getString(
                                R.string.please_select_an_answer
                            ),

                            Toast.LENGTH_SHORT

                        ).show()
                    }
                },

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Text(

                    text =
                        if (
                            currQuestion.isEnabled
                        ) {

                            stringResource(
                                R.string.submit
                            )

                        } else {

                            stringResource(
                                R.string.next
                            )
                        }
                )
            }

        } else {

            Column(

                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.Center,

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                CircularProgressIndicator(

                    color =
                        MaterialTheme.colors.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(

                    text =
                        "Loading question...",

                    color =
                        MaterialTheme.colors.onBackground
                            .copy(alpha = 0.6f)
                )
            }
        }
    }
}

@SuppressLint(
    "UnrememberedMutableState"
)
@Composable
fun QuestionCard(

    questionNo: Int,

    question: Question,

    isEnabled: Boolean,

    currAns: String,

    onClick: (String) -> Unit

) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        // ==================================================
        // QUESTION
        // ==================================================

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    MaterialTheme.shapes.large
                )
                .background(
                    MaterialTheme.colors.surface
                )
                .border(

                    BorderStroke(

                        1.dp,

                        MaterialTheme.colors.onSurface
                            .copy(alpha = 0.08f)
                    ),

                    MaterialTheme.shapes.large
                )

                .padding(22.dp)

        ) {

            Text(

                text =
                    "QUESTION $questionNo",

                style =
                    MaterialTheme.typography.caption,

                color =
                    MaterialTheme.colors.primary,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(

                text =
                    question.question,

                style =
                    MaterialTheme.typography.h6,

                color =
                    MaterialTheme.colors.onSurface
            )
        }

        LazyColumn(

            modifier =
                Modifier.fillMaxWidth(),

            contentPadding =
                PaddingValues(
                    vertical = 16.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)

        ) {

            items(
                question.answers
            ) { answer ->

                val isCorrect =
                    !isEnabled &&
                            answer ==
                            question.correctAnswer

                val isWrongSelection =
                    !isEnabled &&
                            currAns == answer &&
                            answer !=
                            question.correctAnswer

                val background =

                    when {

                        isCorrect ->
                            QuizSuccess.copy(
                                alpha = 0.13f
                            )

                        isWrongSelection ->
                            QuizError.copy(
                                alpha = 0.12f
                            )

                        currAns == answer &&
                                isEnabled ->

                            MaterialTheme.colors.primary
                                .copy(
                                    alpha = 0.10f
                                )

                        else ->
                            MaterialTheme.colors.surface
                    }

                val borderColor =

                    when {

                        isCorrect ->
                            QuizSuccess

                        isWrongSelection ->
                            QuizError

                        currAns == answer &&
                                isEnabled ->

                            MaterialTheme.colors.primary

                        else ->

                            MaterialTheme.colors.onSurface
                                .copy(
                                    alpha = 0.10f
                                )
                    }

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            background
                        )
                        .border(

                            BorderStroke(
                                1.5.dp,
                                borderColor
                            ),

                            RoundedCornerShape(
                                18.dp
                            )
                        )
                        .clickable(
                            enabled = isEnabled
                        ) {

                            onClick(answer)
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        text =
                            answer,

                        modifier =
                            Modifier.weight(1f),

                        color =
                            MaterialTheme.colors.onSurface,

                        fontSize =
                            15.sp
                    )

                    RadioButton(

                        selected =
                            currAns == answer,

                        onClick =
                            null,

                        colors =
                            RadioButtonDefaults.colors(

                                selectedColor =

                                    when {

                                        isCorrect ->
                                            QuizSuccess

                                        isWrongSelection ->
                                            QuizError

                                        else ->
                                            MaterialTheme.colors.primary
                                    },

                                unselectedColor =
                                    MaterialTheme.colors.onSurface
                                        .copy(
                                            alpha = 0.35f
                                        )
                            )
                    )
                }
            }
        }
    }
}