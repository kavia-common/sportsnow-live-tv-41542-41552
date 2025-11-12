package org.sportsnow.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val Primary = Color(0xFF2563EB)
private val Secondary = Color(0xFFF59E0B)
private val Error = Color(0xFFEF4444)
private val Background = Color(0xFFF9FAFB)
private val Surface = Color(0xFFFFFFFF)
private val Text = Color(0xFF111827)

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    error = Error,
    background = Background,
    surface = Surface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onError = Color.White,
    onBackground = Text,
    onSurface = Text
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    error = Error,
    background = Color(0xFF0B1220),
    surface = Color(0xFF111827),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onError = Color.White,
    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFE5E7EB)
)

/**
 * PUBLIC_INTERFACE
 * SportsNowTheme applies both Material3 and TV Material theme for Composables.
 */
@Composable
fun SportsNowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
