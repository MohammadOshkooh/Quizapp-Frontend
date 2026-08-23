package com.example.quizella.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.quizella.domain.repository.ScoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ScoreRepository {

    companion object {
        private val POINTS_KEY = intPreferencesKey("points")
        private val TEMP_POINTS_KEY = intPreferencesKey("temp_points")
    }

    override val getPoints: Flow<Int> = dataStore.data.map { prefs ->
        prefs[POINTS_KEY] ?: 0
    }

    override suspend fun setPoints(points: Int) {
        dataStore.edit { prefs ->
            prefs[POINTS_KEY] = points
        }
    }

    override val getTempPoints: Flow<Int> = dataStore.data.map { prefs ->
        prefs[TEMP_POINTS_KEY] ?: 0
    }

    override suspend fun setTempPoints(points: Int) {
        dataStore.edit { prefs ->
            prefs[TEMP_POINTS_KEY] = points
        }
    }

    override suspend fun clearTempPoints() {
        dataStore.edit { prefs ->
            prefs.remove(TEMP_POINTS_KEY)
        }
    }
}