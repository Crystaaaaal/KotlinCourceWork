package com.example.kotlincoursework.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.ui.theme.state.AppTheme
import com.example.kotlincoursework.ui.theme.state.AppThemes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    private val _availableThemes = MutableStateFlow(AppThemes.AllThemes)
    val availableThemes: StateFlow<List<AppTheme>> = _availableThemes.asStateFlow()

    private val _currentTheme = MutableStateFlow(AppThemes.Light)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    fun setTheme(themeId: String) {
        viewModelScope.launch {
            _availableThemes.value.find { it.id == themeId }?.let { theme ->
                _currentTheme.emit(theme)
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val current = _currentTheme.value
            val currentIndex = _availableThemes.value.indexOfFirst { it.id == current.id }
            val nextIndex = (currentIndex + 1) % _availableThemes.value.size
            _currentTheme.emit(_availableThemes.value[nextIndex])
        }
    }
}