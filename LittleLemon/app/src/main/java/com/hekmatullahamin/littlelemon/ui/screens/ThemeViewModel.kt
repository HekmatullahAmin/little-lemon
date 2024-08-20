package com.hekmatullahamin.littlelemon.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.repositories.UserPreferencesRepository
import com.hekmatullahamin.littlelemon.utils.Constants
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = userPreferencesRepository
        .themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
            initialValue = ThemeMode.LIGHT
        )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemeMode(themeMode)
        }
    }
}