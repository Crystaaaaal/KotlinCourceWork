package com.example.kotlincoursework.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.kotlincoursework.ui.theme.state.AppTheme
import com.example.kotlincoursework.ui.theme.state.AppThemes

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    outline = LightOutline,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    error = LightError
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,

    outline = DarkOutline,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,

    error = DarkError
)

val PurpleColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = PurpleOnPrimary,

    secondary = PurpleSecondary,
    onSecondary = PurpleOnSecondary,

    outline = PurpleOutline,

    background = PurpleBackground,
    onBackground = PurpleOnBackground,

    surface = PurpleSurface,
    onSurface = PurpleOnSurface,

    error = PurpleError
)

val GreenColorScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,

    secondary = GreenSecondary,
    onSecondary = GreenOnSecondary,

    outline = GreenOutline,

    background = GreenBackground,
    onBackground = GreenOnBackground,

    surface = GreenSurface,
    onSurface = GreenOnSurface,

    error = GreenError
)

val OrangeColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = OrangeOnPrimary,

    secondary = OrangeSecondary,
    onSecondary = OrangeOnSecondary,

    outline = OrangeOutline,

    background = OrangeBackground,
    onBackground = OrangeOnBackground,

    surface = OrangeSurface,
    onSurface = OrangeOnSurface,

    error = OrangeError
)

// Local provider для текущей темы
val LocalTheme = staticCompositionLocalOf { AppThemes.Light }

@Composable
fun KotlinCourseWorkTheme(
    theme: AppTheme = AppThemes.Light,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTheme provides theme) {
        MaterialTheme(
            colorScheme = theme.colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Расширенные свойства для доступа к теме
val MaterialTheme.customTheme: AppTheme
    @Composable
    get() = LocalTheme.current
