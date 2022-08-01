package cinescout.test.compose.util

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText

fun hasText(
    @StringRes textRes: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false
): SemanticsMatcher = hasText(
    text = getString(textRes),
    substring = substring,
    ignoreCase = ignoreCase
)

fun hasContentDescription(
    @StringRes textRes: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false
): SemanticsMatcher = hasContentDescription(
    value = getString(textRes),
    substring = substring,
    ignoreCase = ignoreCase
)
