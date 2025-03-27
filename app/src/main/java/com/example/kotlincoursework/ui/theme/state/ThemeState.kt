package com.example.kotlincoursework.ui.theme.state

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.kotlincoursework.ui.theme.DarkColorScheme
import com.example.kotlincoursework.ui.theme.GreenColorScheme
import com.example.kotlincoursework.ui.theme.LightColorScheme
import com.example.kotlincoursework.ui.theme.OrangeColorScheme
import com.example.kotlincoursework.ui.theme.PurpleColorScheme

@Immutable
data class AppTheme(
    val id: String,
    val name: String,
    val colorScheme: ColorScheme,
)

// Предопределенные темы
object AppThemes {
    val Light = AppTheme(
        id = "light",
        name = "Светлая",
        colorScheme = LightColorScheme
    )

    val Dark = AppTheme(
        id = "dark",
        name = "Тёмная",
        colorScheme = DarkColorScheme
    )

    val Purple = AppTheme(
        id = "purple",
        name = "Фиолетовая",
        colorScheme = PurpleColorScheme
    )

    val Green = AppTheme(
        id = "green",
        name = "Зеленая",
        colorScheme = GreenColorScheme
    )
    val Orange = AppTheme(
        id = "orange",
        name = "Оранжевая",
        colorScheme = OrangeColorScheme
    )

    val AllThemes = listOf(Light, Dark, Purple, Green, Orange)
}