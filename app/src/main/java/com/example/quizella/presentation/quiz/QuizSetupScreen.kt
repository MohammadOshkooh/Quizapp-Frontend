package com.example.quizella.presentation.quiz

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.quizella.domain.model.Category
import com.example.quizella.presentation.util.Screen

@Composable
fun QuizSetupScreen(
    navHostController: NavHostController,
    viewModel: QuizSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    navHostController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Quiz Setup",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Choose your quiz options",
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SelectionCard(
                        title = "Category"
                    ) {
                        CategorySelector(
                            categories = state.categories,
                            selected = state.selectedCategory,
                            onSelected = viewModel::selectCategory
                        )
                    }
                }

                item {
                    SelectionCard(
                        title = "Question Type"
                    ) {
                        QuestionTypeOption(
                            title = "Text",
                            value = "text",
                            selectedTypes = state.selectedTypes,
                            onToggle = viewModel::toggleQuestionType
                        )

                        QuestionTypeOption(
                            title = "Audio",
                            value = "audio",
                            selectedTypes = state.selectedTypes,
                            onToggle = viewModel::toggleQuestionType
                        )

                        QuestionTypeOption(
                            title = "Image",
                            value = "image",
                            selectedTypes = state.selectedTypes,
                            onToggle = viewModel::toggleQuestionType
                        )
                    }
                }

                item {
                    SelectionCard(
                        title = "Number of Questions"
                    ) {
                        QuestionCountOption(
                            count = 5,
                            selectedCount = state.selectedCount,
                            onSelect = viewModel::selectCount
                        )

                        QuestionCountOption(
                            count = 10,
                            selectedCount = state.selectedCount,
                            onSelect = viewModel::selectCount
                        )

                        QuestionCountOption(
                            count = 15,
                            selectedCount = state.selectedCount,
                            onSelect = viewModel::selectCount
                        )

                        QuestionCountOption(
                            count = 20,
                            selectedCount = state.selectedCount,
                            onSelect = viewModel::selectCount
                        )
                    }
                }

                item {
                    state.errorMessage?.let { errorMessage ->
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = state.selectedCategory != null &&
                                state.selectedTypes.isNotEmpty(),
                        onClick = {
                            val config = viewModel.buildConfig()

                            if (config != null) {
                                navHostController.navigate(
                                    Screen.QuestionScreen.createRoute(
                                        category = Uri.encode(config.category),
                                        type = Uri.encode(
                                            config.questionTypes.joinToString(",")
                                        ),
                                        count = config.questionCount
                                    )
                                )
                            }
                        }
                    ) {
                        Text("Start Quiz")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun CategorySelector(
    categories: List<Category>,
    selected: Category?,
    onSelected: (Category) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                expanded = true
            }
        ) {
            Text(
                text = selected?.name ?: "Select Category",
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = "Select category"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(category)
                        expanded = false
                    }
                ) {
                    Text(text = category.name)
                }
            }
        }
    }
}

@Composable
private fun QuestionTypeOption(
    title: String,
    value: String,
    selectedTypes: Set<String>,
    onToggle: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onToggle(value)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = value in selectedTypes,
            onCheckedChange = {
                onToggle(value)
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title
        )
    }
}

@Composable
private fun QuestionCountOption(
    count: Int,
    selectedCount: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(count)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedCount == count,
            onClick = {
                onSelect(count)
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$count Questions"
        )
    }
}

@Composable
private fun SelectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}