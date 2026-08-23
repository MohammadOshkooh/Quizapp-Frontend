package com.example.quizella.common

object Constants {

    /**
     * Base URL of the local FastAPI backend.
     *
     * 10.0.2.2 points to the host machine when using
     * the Android Emulator.
     */
    const val API_BASE_URL = "http://10.0.2.2:8000"

    /**
     * Questions endpoint.
     */
    const val QUESTIONS_URL =
        "$API_BASE_URL/api/questions/?limit=10&category=science%2Chistory"

    /**
     * Authentication endpoints.
     */
    const val REGISTER_URL =
        "$API_BASE_URL/api/auth/register"

    const val LOGIN_URL =
        "$API_BASE_URL/api/auth/login"
}