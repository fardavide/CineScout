package cinescout.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.NoContentDescription
import cinescout.resources.R.string

@Composable
fun CsBottomBar(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier.testTag(TestTag.BottomBar).background(MaterialTheme.colorScheme.secondaryContainer)) {
        Row(modifier = Modifier.navigationBarsPadding()) {
            icon()
            Spacer(modifier = Modifier.weight(1f))
            actions()
        }
    }
}

@Composable
fun BackBottomBar(modifier: Modifier = Modifier, back: () -> Unit) {
    CsBottomBar(
        modifier = modifier,
        icon = {
            IconButton(onClick = back) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = string.back_button_description)
                )
            }
        }
    )
}

@Preview
@Composable
private fun CineScoutBottomBarPreview() {
    CineScoutTheme {
        CsBottomBar(
            icon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = NoContentDescription)
                }
            }
        )
    }
}

@Preview
@Composable
private fun BackBottomBarPreview() {
    CineScoutTheme {
        BackBottomBar(back = {})
    }
}
