package com.example.quizella.presentation.results

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizella.domain.repository.QuizResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val quizResultRepository: QuizResultRepository
) : ViewModel() {

    private val _state = mutableStateOf(ResultsState())
    val state: State<ResultsState> = _state

    init {
        loadResults()
    }

    fun loadResults() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = quizResultRepository.getQuizResults()
            _state.value = result.fold(
                onSuccess = { results ->
                    ResultsState(
                        isLoading = false,
                        results = results
                    )
                },
                onFailure = { error ->
                    ResultsState(
                        isLoading = false,
                        errorMessage = error.message
                            ?: "Unable to load quiz history."
                    )
                }
            )
        }
    }
}
