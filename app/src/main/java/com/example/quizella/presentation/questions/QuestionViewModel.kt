/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

package com.example.quizella.presentation.questions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.quizella.domain.model.Question
import com.example.quizella.domain.repository.QuestionsRepository
import com.example.quizella.domain.repository.ScoreRepository
import com.example.quizella.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _currQuestionState = mutableStateOf(QuestionState())
    val currQuestionState: State<QuestionState> = _currQuestionState

    private val _questionsList = mutableStateOf(QuestionListState())
    val questionsList: State<QuestionListState> = _questionsList


    init {

        viewModelScope.launch {
            questionsRepository.getQuestions().collect {
                _questionsList.value = questionsList.value.copy(
                    list = it.toMutableList()
                )
                // Check if list is not empty before accessing index 0
                if (it.isNotEmpty()) {
                    _currQuestionState.value = currQuestionState.value.copy(
                        questionNo = 1,
                        currQuestion = it[0],
                        correctCount = 0  // Reset correct count
                    )
                } else {
                    // Handle empty list - set default state
                    _currQuestionState.value = currQuestionState.value.copy(
                        questionNo = 0,
                        currQuestion = null
                    )
                    Log.e("QuestionViewModel", "No questions received from API")
                }
            }
            scoreRepository.getPoints.collect {
                _questionsList.value = questionsList.value.copy(
                    points = it,
                    tempPoints = 0
                )
            }
        }
    }

    private val _dialogState = mutableStateOf(DialogState())
    val dialogState: State<DialogState> = _dialogState


    fun onEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.NextQuestion -> {

                if (_currQuestionState.value.isEnabled) {
                    _currQuestionState.value = currQuestionState.value.copy(
                        isEnabled = false
                    )
                } else {
                    // Check if the answer is correct
                    val isCorrect = _currQuestionState.value.currQuestion?.correctAnswer == _currQuestionState.value.currAns

                    // Update correct count if answer is correct
                    if (isCorrect) {
                        _currQuestionState.value = currQuestionState.value.copy(
                            correctCount = currQuestionState.value.correctCount + 1
                        )
                    }

                    // Get current question number (1-based)
                    val currentQNo = _currQuestionState.value.questionNo ?: 0
                    val totalQuestions = _questionsList.value.list.size

                    // Check if there is a next question available
                    if (currentQNo < totalQuestions) {
                        // There is a next question, move to it
                        _currQuestionState.value = currQuestionState.value.copy(
                            questionNo = currentQNo + 1,
                            currQuestion = _questionsList.value.list[currentQNo]  // index is currentQNo (0-based)
                        )
                        _currQuestionState.value = currQuestionState.value.copy(
                            isEnabled = true,
                            currAns = ""
                        )
                    } else {
                        // No more questions, navigate to finish screen
                        event.navHostController.navigate(Screen.FinishScreen.route) {
                            popUpTo(event.navHostController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                        viewModelScope.launch {
                            val finalScore = _currQuestionState.value.correctCount  // Use correct count
                            _questionsList.value = questionsList.value.copy(
                                tempPoints = finalScore
                            )
                            viewModelScope.launch {
                                scoreRepository.setTempPoints(finalScore)
                                scoreRepository.getTempPoints.collect { temporaryPoints ->
                                    scoreRepository.getPoints.collect { points ->
                                        if (points < temporaryPoints) {
                                            scoreRepository.setPoints(temporaryPoints)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is QuestionEvent.ChangeAnswer -> {
                _currQuestionState.value = currQuestionState.value.copy(
                    currAns = event.ans
                )
            }

            is QuestionEvent.OpenDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = true
                )
            }
            is QuestionEvent.CloseDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = false
                )
            }
        }
    }
}