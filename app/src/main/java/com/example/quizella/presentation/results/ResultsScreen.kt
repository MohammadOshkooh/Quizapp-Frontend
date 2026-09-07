package com.example.quizella.presentation.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizella.domain.model.QuizResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ResultsScreen(
    navHostController: NavHostController,
    viewModel: ResultsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navHostController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Quiz History",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = viewModel::loadResults
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colors.error
                    )
                }
            }

            state.results.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No quiz results yet.",
                        color = MaterialTheme.colors.onBackground.copy(
                            alpha = 0.6f
                        )
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        state.results,
                        key = { it.id }
                    ) { result ->
                        QuizResultCard(result)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultCard(result: QuizResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.category,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${result.correctCount}/${result.questionCount}",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Score: ${result.score}",
                style = MaterialTheme.typography.body1
            )

            Text(
                text = "Media: ${result.questionTypes.joinToString(", ")}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = 0.65f
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = formatCompletedAt(result.completedAt),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = 0.55f
                )
            )
        }
    }
}

private fun formatCompletedAt(value: String): String {
    return try {
        val normalizedValue = value
            .replace("ُ", "")
            .replace(" ", "T")
            .replace("T", "T", ignoreCase = false)

        val dateTime = LocalDateTime.parse(
            normalizedValue.substringBeforeLast("Z"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )

        dateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
    } catch (e: Exception) {
        value
    }
}