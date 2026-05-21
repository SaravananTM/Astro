package com.horoscope.matcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

// Tamil wedding & temple inspired palette
private val LightColors = lightColorScheme(
    primary = Color(0xFFB8860B),        // Antique gold
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC62828),       // Kumkum red
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF2E7D32),        // Mango leaf green
    background = Color(0xFFFFFBF5),      // Warm cream (manjal tint)
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFFFF3E0),  // Light turmeric
    onBackground = Color(0xFF3E2723),    // Dark brown text
    onSurface = Color(0xFF3E2723),
    onSurfaceVariant = Color(0xFF5D4037),
    outline = Color(0xFFD4A056),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFD54F),         // Bright gold
    onPrimary = Color(0xFF3E2723),
    secondary = Color(0xFFEF9A9A),       // Soft kumkum
    onSecondary = Color(0xFF3E2723),
    tertiary = Color(0xFFA5D6A7),        // Soft green
    background = Color(0xFF1A1210),      // Dark rosewood
    surface = Color(0xFF2C1F1A),         // Dark warm brown
    surfaceVariant = Color(0xFF3D2B22),
    onBackground = Color(0xFFFFF8E1),
    onSurface = Color(0xFFFFF8E1),
    onSurfaceVariant = Color(0xFFD7CCC8),
    outline = Color(0xFFBCAAA4),
)

data class AppColors(
    val boyCard: Color,
    val girlCard: Color,
    val matchGood: Color,
    val matchBad: Color,
    val verdictGood: Color,
    val verdictWarn: Color,
    val recentMatch: Color,
    val scoreGreen: Color,
    val scoreRed: Color,
    val gold: Color,
    val auspicious: Color
)

val LightAppColors = AppColors(
    boyCard = Color(0xFFE8F0FE),         // Soft blue
    girlCard = Color(0xFFFDE8E8),        // Soft rose
    matchGood = Color(0xFFE8F5E9),       // Temple green tint
    matchBad = Color(0xFFFBE9E7),        // Soft warning
    verdictGood = Color(0xFFE8F5E9),
    verdictWarn = Color(0xFFFFF8E1),     // Turmeric warning
    recentMatch = Color(0xFFFFF8E1),     // Golden highlight
    scoreGreen = Color(0xFF2E7D32),      // Temple green
    scoreRed = Color(0xFFC62828),        // Kumkum red
    gold = Color(0xFFB8860B),
    auspicious = Color(0xFFF9A825)
)

val DarkAppColors = AppColors(
    boyCard = Color(0xFF1A2E4A),
    girlCard = Color(0xFF3A1A2A),
    matchGood = Color(0xFF1B3A1B),
    matchBad = Color(0xFF3A1B1B),
    verdictGood = Color(0xFF1B3A1B),
    verdictWarn = Color(0xFF3A3018),
    recentMatch = Color(0xFF3A3018),
    scoreGreen = Color(0xFFA5D6A7),
    scoreRed = Color(0xFFEF9A9A),
    gold = Color(0xFFFFD54F),
    auspicious = Color(0xFFFFE082)
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
