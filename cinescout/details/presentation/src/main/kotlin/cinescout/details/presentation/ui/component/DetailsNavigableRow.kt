package cinescout.details.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import cinescout.design.theme.Dimens
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsNavigableRow(
    onClick: () -> Unit,
    contentDescription: TextRes,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(horizontal = Dimens.Margin.medium)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) { content() }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = string(textRes = contentDescription)
            )
        }
    }
}
