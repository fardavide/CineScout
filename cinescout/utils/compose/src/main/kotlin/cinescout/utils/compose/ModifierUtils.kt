package cinescout.utils.compose

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier =
    if (condition) block() else this
