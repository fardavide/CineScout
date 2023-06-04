package cinescout.details.presentation.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsAssistChip
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DetailsGenres(genres: ImmutableList<String>) {
    val spacing = Dimens.Margin.XSmall
    FlowRow(
        mainAxisAlignment = FlowMainAxisAlignment.Center,
        mainAxisSpacing = spacing,
        crossAxisSpacing = spacing
    ) {
        for (genre in genres) {
            CsAssistChip { Text(text = genre) }
        }
    }
}
