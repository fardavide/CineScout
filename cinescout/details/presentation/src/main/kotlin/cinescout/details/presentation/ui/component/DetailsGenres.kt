package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsAssistChip
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DetailsGenres(genres: ImmutableList<String>, modifier: Modifier = Modifier) {
    val spacing = Dimens.Margin.xSmall
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing, alignment = Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(spacing, alignment = Alignment.CenterVertically)
    ) {
        for (genre in genres) {
            CsAssistChip { Text(text = genre) }
        }
    }
}
