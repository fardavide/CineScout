package cinescout.design.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.surfaceUltraThin
import cinescout.utils.compose.thenIfNotNull

private val containerColor @Composable get() = MaterialTheme.colorScheme.surfaceUltraThin

@Composable
fun CsCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    onclick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .clip(shape)
            .thenIfNotNull(onclick) { clickable(onClick = it) },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        content = content,
        shape = shape
    )
}

@Preview
@Composable
private fun CsCardPreview() {
    CineScoutTheme {
        CsCard {
            Text(
                modifier = Modifier.padding(Dimens.Margin.medium),
                text = "Card content"
            )
        }
    }
}
