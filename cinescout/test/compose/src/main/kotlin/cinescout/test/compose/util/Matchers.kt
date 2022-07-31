package cinescout.test.compose.util

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText

fun hasText(
    @StringRes textRes: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false
): SemanticsMatcher = hasText(getString(textRes), substring, ignoreCase)

fun SemanticsNodeInteraction.assertTextContains(
    @StringRes valueRes: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false
): SemanticsNodeInteraction = assertTextContains(getString(valueRes), substring, ignoreCase)
