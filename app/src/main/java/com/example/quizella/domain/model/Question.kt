package com.example.quizella.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions_table")
data class Question(
    val category: String,

    @PrimaryKey
    val id: String,

    val correctAnswer: String,

    val answers: List<String>,

    val quizToken: String,

    val tags: List<String>,

    val question: String,

    // Answer mechanism.
    // Always multiple_choice.
    val type: String,

    // Actual content/media type.
    // text / audio / image
    val mediaType: String,

    val difficulty: String,

    val regions: List<String>?,

    val isNiche: Boolean,

    val mediaUrl: String?
)