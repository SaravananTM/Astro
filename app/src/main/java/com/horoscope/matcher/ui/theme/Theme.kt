package com.horoscope.matcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFFE91E63),
    tertiary = Color(0xFFFF9800),
    background = Color(0xFFFFF8F0),
    surface = Color(0xFFFFFFFF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFF48FB1),
    tertiary = Color(0xFFFFCC80),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2B2930),
)

// Custom colors that adapt to theme
data class AppColors(
    val boyCard: Color,
    val girlCard: Color,
    val matchGood: Color,
    val matchBad: Color,
    val verdictGood: Color,
    val verdictWarn: Color,
    val recentMatch: Color,
    val scoreGreen: Color,
    val scoreRed: Color
)

val LightAppColors = AppColors(
    boyCard = Color(0xFFE3F2FD),
    girlCard = Color(0xFFFCE4EC),
    matchGood = Color(0xFFE8F5E9),
    matchBad = Color(0xFFFFEBEE),
    verdictGood = Color(0xFFE8F5E9),
    verdictWarn = Color(0xFFFFF3E0),
    recentMatch = Color(0xFFF3E5F5),
    scoreGreen = Color(0xFF4CAF50),
    scoreRed = Color(0xFFFF5722)
)

val DarkAppColors = AppColors(
    boyCard = Color(0xFF1A3A5C),
    girlCard = Color(0xFF4A2030),
    matchGood = Color(0xFF1B3A1B),
    matchBad = Color(0xFF3A1B1B),
    verdictGood = Color(0xFF1B3A1B),
    verdictWarn = Color(0xFF3A3018),
    recentMatch = Color(0xFF2D1F3D),
    scoreGreen = Color(0xFF81C784),
    scoreRed = Color(0xFFFF8A65)
)

val LocalAppColors = compositionLocalOf { LightAppColors }

@Composable
fun HoroscopeTheme(content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val colors = if (isDark) DarkColors else LightColors
    val appColors = if (isDark) DarkAppColors else LightAppColors

    androidx.compose.runtime.CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}
