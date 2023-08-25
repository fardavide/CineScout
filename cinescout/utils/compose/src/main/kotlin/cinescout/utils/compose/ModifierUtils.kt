package cinescout.utils.compose

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier =
    if (condition) block() else this

inline fun <T : Any> Modifier.thenIfNotNull(value: T?, block: Modifier.(T) -> Modifier): Modifier =
    if (value != null) block(value) else this
