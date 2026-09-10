package com.example.quizella.presentation.questions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.quizella.domain.repository.QuestionsRepository
import com.example.quizella.domain.repository.QuizResultRepository
import com.example.quizella.domain.repository.ScoreRepository
import com.example.quizella.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val scoreRepository: ScoreRepository,
    private val quizResultRepository: QuizResultRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val category: String =
        savedStateHandle.get<String>("category")
            ?: ""

    /*
     * The existing Navigation argument is named "type".
     *
     * QuizSetupScreen already sends:
     *
     * text
     * text,audio
     * text,image
     * audio,image
     * text,audio,image
     *
     * We keep that existing route and parse it here.
     */
    private val questionTypes: List<String> =
        (
                savedStateHandle
                    .get<String>("type")
                    ?: "text"
                )
            .split(",")
            .map {
                it.trim().lowercase()
            }
            .filter {
                it in setOf(
                    "text",
                    "audio",
                    "image"
                )
            }
            .distinct()
            .ifEmpty {
                listOf("text")
            }

    private val count: Int =
        savedStateHandle
            .get<String>("count")
            ?.toIntOrNull()
            ?: savedStateHandle
                .get<Int>("count")
            ?: 10

    private val answeredHistoryIds =
        mutableListOf<Int>()

    private val _currQuestionState =
        mutableStateOf(
            QuestionState()
        )

    val currQuestionState:
            State<QuestionState> =
        _currQuestionState

    private val _questionsList =
        mutableStateOf(
            QuestionListState()
        )

    val questionsList:
            State<QuestionListState> =
        _questionsList

    private val _dialogState =
        mutableStateOf(
            DialogState()
        )

    val dialogState:
            State<DialogState> =
        _dialogState

    init {

        loadQuestions()

        viewModelScope.launch {

            scoreRepository
                .getPoints
                .collect { points ->

                    _questionsList.value =
                        _questionsList.value.copy(
                            points = points
                        )
                }
        }
    }

    private fun loadQuestions() {

        if (
            category.isBlank()
            || questionTypes.isEmpty()
        ) {

            _currQuestionState.value =
                _currQuestionState.value.copy(
                    questionNo = 0,
                    currQuestion = null,
                    errorMessage =
                        "Quiz configuration is missing."
                )

            return
        }

        viewModelScope.launch {

            try {

                questionsRepository
                    .getQuestions(
                        category = category,
                        questionTypes =
                            questionTypes,
                        limit = count
                    )
                    .collect { questions ->

                        _questionsList.value =
                            _questionsList.value.copy(
                                list =
                                    questions.toMutableList()
                            )

                        if (questions.isNotEmpty()) {

                            _currQuestionState.value =
                                _currQuestionState.value.copy(
                                    questionNo = 1,
                                    currQuestion =
                                        questions.first(),
                                    isEnabled = true,
                                    currAns = "",
                                    correctCount = 0,
                                    errorMessage = null
                                )

                        } else {

                            _currQuestionState.value =
                                _currQuestionState.value.copy(
                                    questionNo = 0,
                                    currQuestion = null,
                                    errorMessage =
                                        "No questions are available for " +
                                                "$category with the selected " +
                                                "question types."
                                )

                            Log.e(
                                "QuestionViewModel",
                                "No questions for " +
                                        "$category / " +
                                        questionTypes.joinToString(", ")
                            )
                        }
                    }

            } catch (e: Exception) {

                Log.e(
                    "QuestionViewModel",
                    "Failed to load questions",
                    e
                )

                _currQuestionState.value =
                    _currQuestionState.value.copy(
                        questionNo = 0,
                        currQuestion = null,
                        errorMessage =
                            "Unable to load the quiz. " +
                                    "Check that the backend is running."
                    )
            }
        }
    }

    fun onEvent(
        event: QuestionEvent
    ) {

        when (event) {

            is QuestionEvent.NextQuestion -> {

                /*
                 * First click = submit the answer to the backend.
                 * The server is the only authority for correctness.
                 */
                if (
                    _currQuestionState
                        .value
                        .isEnabled
                ) {

                    val current =
                        _currQuestionState.value

                    val question =
                        current.currQuestion

                    if (question == null) {
                        return
                    }

                    viewModelScope.launch {

                        val result =
                            questionsRepository
                                .submitAnswer(
                                    questionId =
                                        question.id
                                            .toIntOrNull()
                                            ?: return@launch,

                                    selectedAnswer =
                                        current.currAns,

                                    quizToken =
                                        question.quizToken
                                )

                        result.fold(

                            onSuccess = {
                                    answerResult ->

                                val (
                                    correctAnswer,
                                    historyId
                                ) = answerResult

                                val isCorrect =
                                    current.currAns ==
                                            correctAnswer

                                answeredHistoryIds
                                    .add(
                                        historyId
                                    )

                                _currQuestionState.value =
                                    _currQuestionState
                                        .value
                                        .copy(

                                            isEnabled =
                                                false,

                                            currQuestion =
                                                question.copy(
                                                    correctAnswer =
                                                        correctAnswer
                                                ),

                                            correctCount =
                                                if (isCorrect) {
                                                    current.correctCount + 1
                                                } else {
                                                    current.correctCount
                                                },

                                            errorMessage =
                                                null
                                        )
                            },

                            onFailure = {
                                    error ->

                                Log.e(
                                    "QuestionViewModel",
                                    "Failed to submit answer",
                                    error
                                )

                                _currQuestionState.value =
                                    _currQuestionState
                                        .value
                                        .copy(
                                            errorMessage =
                                                "Unable to submit your answer. " +
                                                        "Please try again."
                                        )
                            }
                        )
                    }

                    return
                }

                /*
                 * Second click = next question.
                 */
                val current =
                    _currQuestionState.value

                val currentQNo =
                    current.questionNo ?: 0

                val totalQuestions =
                    _questionsList
                        .value
                        .list
                        .size

                if (
                    currentQNo < totalQuestions
                ) {

                    _currQuestionState.value =
                        current.copy(

                            questionNo =
                                currentQNo + 1,

                            currQuestion =
                                _questionsList
                                    .value
                                    .list[currentQNo],

                            isEnabled = true,

                            currAns = "",

                            errorMessage = null
                        )

                } else {

                    /*
                     * No more questions.
                     */
                    val finalScore =
                        current.correctCount

                    _questionsList.value =
                        _questionsList.value.copy(
                            tempPoints =
                                finalScore
                        )

                    viewModelScope.launch {

                        scoreRepository
                            .setTempPoints(
                                finalScore
                            )

                        val currentBest =
                            scoreRepository
                                .getPoints
                                .first()

                        if (
                            currentBest < finalScore
                        ) {

                            scoreRepository
                                .setPoints(
                                    finalScore
                                )
                        }

                        if (
                            answeredHistoryIds.size
                            != totalQuestions
                        ) {

                            _currQuestionState.value =
                                _currQuestionState
                                    .value
                                    .copy(
                                        errorMessage =
                                            "Some answers were not saved. " +
                                                    "Please try again."
                                    )

                            return@launch
                        }

                        val quizToken =
                            _questionsList
                                .value
                                .list
                                .firstOrNull()
                                ?.quizToken

                        if (
                            quizToken.isNullOrBlank()
                        ) {

                            _currQuestionState.value =
                                _currQuestionState
                                    .value
                                    .copy(
                                        errorMessage =
                                            "Quiz session is invalid. " +
                                                    "Please restart the quiz."
                                    )

                            return@launch
                        }

                        val saveResult =
                            quizResultRepository
                                .saveQuizResult(
                                    quizToken =
                                        quizToken,

                                    historyIds =
                                        answeredHistoryIds
                                            .toList()
                                )

                        if (
                            saveResult.isFailure
                        ) {

                            Log.e(
                                "QuestionViewModel",
                                "Failed to save quiz result",
                                saveResult.exceptionOrNull()
                            )

                            _currQuestionState.value =
                                _currQuestionState
                                    .value
                                    .copy(
                                        errorMessage =
                                            "Unable to save the quiz result. " +
                                                    "Please try again."
                                    )

                            return@launch
                        }

                        event.navHostController
                            .navigate(
                                Screen.FinishScreen.route
                            ) {

                                popUpTo(
                                    event.navHostController
                                        .graph
                                        .findStartDestination()
                                        .id
                                ) {
                                    inclusive = true
                                }
                            }
                    }
                }
            }

            is QuestionEvent.ChangeAnswer -> {

                if (
                    _currQuestionState
                        .value
                        .isEnabled
                ) {

                    _currQuestionState.value =
                        _currQuestionState
                            .value
                            .copy(
                                currAns =
                                    event.ans
                            )
                }
            }

            is QuestionEvent.OpenDialog -> {

                _dialogState.value =
                    _dialogState
                        .value
                        .copy(
                            state = true
                        )
            }

            is QuestionEvent.CloseDialog -> {

                _dialogState.value =
                    _dialogState
                        .value
                        .copy(
                            state = false
                        )
            }
        }
    }
}