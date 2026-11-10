package com.saarezra.moonkeep.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MoonKeepColorScheme = darkColorScheme(
    primary = MoonGold,
    secondary = MoonTeal,
    tertiary = MoonRose,
    background = MoonNightDeep,
    surface = MoonNight,
    onPrimary = MoonNightDeep,
    onBackground = MoonSilver,
    onSurface = MoonSilver
)

@Composable
fun MoonKeepTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MoonKeepColorScheme,
        typography = MoonKeepTypography,
        content = content
    )
}
