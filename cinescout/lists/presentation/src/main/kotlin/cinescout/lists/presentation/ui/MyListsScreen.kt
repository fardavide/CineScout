package cinescout.lists.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import studio.forface.cinescout.design.R.string

@Composable
fun MyListsScreen(actions: MyListsScreen.Actions, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .testTag(TestTag.MyLists)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Large, alignment = Alignment.CenterVertically)
    ) {
        ListItem(title = TextRes(string.lists_rated), onClick = actions.onRatedClick)
        ListItem(title = TextRes(string.lists_liked), onClick = actions.onLikedClick)
        ListItem(title = TextRes(string.lists_disliked), onClick = actions.onDislikedClick)
    }
}

object MyListsScreen {

    data class Actions(
        val onDislikedClick: () -> Unit,
        val onLikedClick: () -> Unit,
        val onRatedClick: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onDislikedClick = {},
                onLikedClick = {},
                onRatedClick = {}
            )
        }
    }
}

@Composable
private fun ListItem(title: TextRes, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick) {
        Text(
            modifier = Modifier.padding(Dimens.Margin.Large),
            text = string(textRes = title)
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
fun MyListsScreenPreview() {
    CineScoutTheme {
        MyListsScreen(MyListsScreen.Actions.Empty)
    }
}
