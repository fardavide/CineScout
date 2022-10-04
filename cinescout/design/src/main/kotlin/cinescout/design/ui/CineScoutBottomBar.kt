package cinescout.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.NoContentDescription

@Composable
fun CineScoutBottomBar(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.navigationBarsPadding()) {
            icon()
            Spacer(modifier = Modifier.weight(1f))
            actions()
        }
    }
}

@Composable
@Preview
private fun CineScoutBottomBarPreview() {
    CineScoutTheme {
        CineScoutBottomBar(
            icon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = NoContentDescription)
                }
            }
        )
    }
}
