package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DarkCharcoal,
    onPrimary = CreamSurface,
    primaryContainer = AmberPrimary,
    onPrimaryContainer = CreamSurface,
    secondary = MutedBrown,
    onSecondary = CreamSurface,
    secondaryContainer = CreamSurfaceVariant,
    onSecondaryContainer = DarkCharcoal,
    tertiary = CoralAccent,
    onTertiary = CreamSurface,
    background = CreamBackground,
    onBackground = DarkCharcoal,
    surface = CreamSurface,
    onSurface = DarkCharcoal,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = MutedBrown,
    outline = BorderBeige,
    outlineVariant = BorderBeige
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkText,
    onPrimary = DarkBackground,
    primaryContainer = AmberPrimary,
    onPrimaryContainer = CreamSurface,
    secondary = DarkTextMuted,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkText,
    tertiary = CoralAccent,
    onTertiary = CreamSurface,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextMuted,
    outline = DarkBorder,
    outlineVariant = DarkBorder
)

@Composable
fun EliasStickersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
