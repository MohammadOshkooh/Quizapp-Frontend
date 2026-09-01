package com.example.quizella.presentation.quiz

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizella.domain.model.Category
import com.example.quizella.domain.model.QuizConfig
import com.example.quizella.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSetupViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = mutableStateOf(QuizSetupState())
    val state: State<QuizSetupState> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val categories = categoryRepository.getCategories()

                _state.value = _state.value.copy(
                    categories = categories,
                    selectedCategory = categories.firstOrNull(),
                    isLoading = false,
                    errorMessage = if (categories.isEmpty()) {
                        "No categories are available. Run the backend seed script first."
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to load categories. Check that the backend is running."
                )
            }
        }
    }

    fun selectCategory(category: Category) {
        _state.value = _state.value.copy(
            selectedCategory = category
        )
    }

    fun toggleQuestionType(type: String) {
        val currentTypes = _state.value.selectedTypes.toMutableSet()

        if (currentTypes.contains(type)) {
            currentTypes.remove(type)
        } else {
            currentTypes.add(type)
        }

        _state.value = _state.value.copy(
            selectedTypes = currentTypes
        )
    }

    fun selectCount(count: Int) {
        _state.value = _state.value.copy(
            selectedCount = count
        )
    }

    fun buildConfig(): QuizConfig? {
        val current = _state.value
        val category = current.selectedCategory ?: return null

        if (current.selectedTypes.isEmpty()) {
            return null
        }

        return QuizConfig(
            category = category.name,
            questionTypes = current.selectedTypes.toList(),
            questionCount = current.selectedCount
        )
    }
}