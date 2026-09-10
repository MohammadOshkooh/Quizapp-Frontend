package com.example.quizella.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.quizella.data.data_source.local.QuestionCacheDao
import com.example.quizella.data.data_source.local.QuestionsCacheDB
import com.example.quizella.data.data_source.local.UserDao
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.data.repository.AuthRepositoryImpl
import com.example.quizella.data.repository.CategoryRepositoryImpl
import com.example.quizella.data.repository.QuestionsRepositoryImpl
import com.example.quizella.data.repository.QuizResultRepositoryImpl
import com.example.quizella.data.repository.ScoreRepositoryImpl
import com.example.quizella.domain.repository.AuthRepository
import com.example.quizella.domain.repository.CategoryRepository
import com.example.quizella.domain.repository.QuestionsRepository
import com.example.quizella.domain.repository.QuizResultRepository
import com.example.quizella.domain.repository.ScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import javax.inject.Singleton

private val Context.quizellaDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "quizella_prefs"
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
        HttpClient {

            install(JsonFeature) {

                serializer =
                    KotlinxSerializer(
                        Json {

                            ignoreUnknownKeys =
                                true

                            isLenient =
                                true

                            encodeDefaults =
                                true
                        }
                    )
            }
        }

    @Provides
    @Singleton
    fun provideTriviaService(
        client: HttpClient
    ): TriviaService =
        TriviaService(client)

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        context.quizellaDataStore

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): QuestionsCacheDB =
        Room.databaseBuilder(
            context,
            QuestionsCacheDB::class.java,
            "questions_cache_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideQuestionDao(
        database: QuestionsCacheDB
    ): QuestionCacheDao =
        database.questionDao()

    @Provides
    @Singleton
    fun provideUserDao(
        database: QuestionsCacheDB
    ): UserDao =
        database.userDao()

    @Provides
    @Singleton
    fun provideQuestionsRepository(
        questionDao: QuestionCacheDao,
        triviaService: TriviaService,
        authRepository: AuthRepository
    ): QuestionsRepository =
        QuestionsRepositoryImpl(
            questionDao,
            triviaService,
            authRepository
        )

    @Provides
    @Singleton
    fun provideCategoryRepository(
        triviaService: TriviaService
    ): CategoryRepository =
        CategoryRepositoryImpl(
            triviaService
        )

    @Provides
    @Singleton
    fun provideQuizResultRepository(
        triviaService: TriviaService,
        authRepository: AuthRepository
    ): QuizResultRepository =
        QuizResultRepositoryImpl(
            triviaService,
            authRepository
        )

    @Provides
    @Singleton
    fun provideScoreRepository(
        dataStore: DataStore<Preferences>
    ): ScoreRepository =
        ScoreRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        triviaService: TriviaService,
        @ApplicationContext context: Context
    ): AuthRepository =
        AuthRepositoryImpl(
            context = context,
            triviaService = triviaService
        )
}