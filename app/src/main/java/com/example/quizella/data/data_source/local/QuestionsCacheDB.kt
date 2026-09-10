package com.example.quizella.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizella.domain.model.Question
import com.example.quizella.domain.model.User

@Database(
    entities = [
        Question::class,
        User::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuestionsCacheDB : RoomDatabase() {

    abstract fun questionDao(): QuestionCacheDao

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "questions_db"
    }
}