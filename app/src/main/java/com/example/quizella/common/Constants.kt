package com.example.quizella.common

object Constants {
    const val API_BASE_URL = "http://10.0.2.2:8000"

    const val QUESTIONS_URL =
        "$API_BASE_URL/api/questions/"

    const val CATEGORIES_URL =
        "$API_BASE_URL/api/categories/"

    const val REGISTER_URL =
        "$API_BASE_URL/api/auth/register"

    const val LOGIN_URL =
        "$API_BASE_URL/api/auth/login"

    const val RESULTS_URL =
        "$API_BASE_URL/api/results/"

    const val ANSWER_URL =
        "$API_BASE_URL/api/questions/answer"
}