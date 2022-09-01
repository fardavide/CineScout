package cinescout.lists.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
fun MyListsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .testTag(TestTag.MyLists)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ListItem(title = TextRes(string.lists_rated))
        ListItem(title = TextRes(string.lists_liked))
        ListItem(title = TextRes(string.lists_disliked))
    }
}

@Composable
private fun ListItem(title: TextRes) {
    Text(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large
            )
            .padding(Dimens.Margin.XLarge),
        text = string(textRes = title)
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
fun MyListsScreenPreview() {
    CineScoutTheme {
        MyListsScreen()
    }
}
