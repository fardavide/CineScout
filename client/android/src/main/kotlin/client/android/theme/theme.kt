package client.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import design.Color
import design.ColorScheme
import design.Palette

private val LightThemeColors = with(Palette(ColorScheme.Light)) {
    lightColors(
        primary = primary.toComposeColor(),
        primaryVariant = primaryDark.toComposeColor(),
        secondary = secondary.toComposeColor(),
        secondaryVariant = secondaryDark.toComposeColor(),
        background = background.toComposeColor(),
        surface = surface.toComposeColor(),
    )
}

private val DarkThemeColors = with(Palette(ColorScheme.Dark)) {
    darkColors(
        primary = primary.toComposeColor(),
        primaryVariant = primaryDark.toComposeColor(),
        secondary = secondary.toComposeColor(),
        background = background.toComposeColor(),
        surface = surface.toComposeColor(),
    )
}

@Composable
fun CineScoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private fun Color.toComposeColor() = androidx.compose.ui.graphics.Color(hex)
