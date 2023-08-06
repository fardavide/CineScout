package cinescout.search.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cinescout.design.theme.Dimens
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun SearchErrorText(message: TextRes) {
    Text(
        modifier = Modifier.padding(Dimens.Margin.small),
        text = string(textRes = message),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}
