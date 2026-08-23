package com.example.quizella.presentation.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizella.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {

        private val THEME_KEY =
            stringPreferencesKey("quizella_theme")
    }

    val theme: StateFlow<ThemeMode> =

        dataStore.data

            .map { preferences ->

                when (
                    preferences[THEME_KEY]
                ) {

                    ThemeMode.NATURE_GREEN.name ->
                        ThemeMode.NATURE_GREEN

                    else ->
                        ThemeMode.MINIMAL_LIGHT
                }
            }

            .stateIn(

                scope = viewModelScope,

                started =
                    SharingStarted.WhileSubscribed(
                        5_000
                    ),

                initialValue =
                    ThemeMode.MINIMAL_LIGHT
            )

    fun setTheme(
        themeMode: ThemeMode
    ) {

        viewModelScope.launch {

            dataStore.edit { preferences ->

                preferences[THEME_KEY] =
                    themeMode.name
            }
        }
    }

    fun toggleTheme() {

        val nextTheme =

            if (
                theme.value ==
                ThemeMode.MINIMAL_LIGHT
            ) {

                ThemeMode.NATURE_GREEN

            } else {

                ThemeMode.MINIMAL_LIGHT
            }

        setTheme(nextTheme)
    }
}