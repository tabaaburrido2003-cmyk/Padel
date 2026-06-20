package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VoltGreen,
    onPrimary = TextBlack,
    primaryContainer = DarkVoltGreen,
    onPrimaryContainer = Color.White,
    secondary = PadelBlue,
    onSecondary = Color.White,
    secondaryContainer = DarkPadelBlue,
    onSecondaryContainer = Color.White,
    background = Color(0xFF131416), // A sleek deep dark theme alternative
    onBackground = Color.White,
    surface = Color(0xFF1E2022),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2E3135),
    onSurfaceVariant = Color.White,
    error = DangerRed,
    onError = Color.Black,
    outline = TextGray
)

private val LightColorScheme = lightColorScheme(
    primary = PadelBlue,
    onPrimary = Color.White,
    primaryContainer = LightPadelBlue,
    onPrimaryContainer = DarkPadelBlue,
    secondary = VoltGreen,
    onSecondary = TextBlack,
    secondaryContainer = LightVoltGreen,
    onSecondaryContainer = TextBlack,
    background = SlateBackground, // #F3F4F9
    onBackground = TextBlack, // #1A1C1E
    surface = SlateSurface, // White
    onSurface = TextBlack, // #1A1C1E
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextGray,
    error = DangerRed,
    onError = Color.White,
    outline = TextGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We enforce our premium sports theme palette
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
