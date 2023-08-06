package cinescout.details.presentation.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsAssistChip
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DetailsGenres(genres: ImmutableList<String>, modifier: Modifier = Modifier) {
    val spacing = Dimens.Margin.xSmall
    FlowRow(
        modifier = modifier,
        mainAxisAlignment = FlowMainAxisAlignment.Center,
        mainAxisSpacing = spacing,
        mainAxisSize = SizeMode.Expand,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        crossAxisSpacing = spacing
    ) {
        for (genre in genres) {
            CsAssistChip { Text(text = genre) }
        }
    }
}
