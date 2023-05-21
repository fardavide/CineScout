package cinescout.design.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens

private const val ContainerAlpha = 0.15f

private val containerColor @Composable get() = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = ContainerAlpha)

@Composable
fun CsCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        content = content,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    )
}

@Preview
@Composable
private fun CsCardPreview() {
    CineScoutTheme {
        CsCard {
            Text(
                modifier = Modifier.padding(Dimens.Margin.Medium),
                text = "Card content"
            )
        }
    }
}
