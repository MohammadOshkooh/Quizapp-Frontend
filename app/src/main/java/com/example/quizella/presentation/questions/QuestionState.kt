package com.example.quizella.presentation.questions

import com.example.quizella.domain.model.Question

data class QuestionState(
    val questionNo: Int? = 0,
    val currQuestion: Question? = null,
    val isEnabled: Boolean = true,
    val currAns: String = "",
    val correctCount: Int = 0,
    val errorMessage: String? = null
)
