package cinescout.lists.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import studio.forface.cinescout.design.R.string

@Composable
fun MyListsScreen(modifier: Modifier = Modifier) {
    Text(text = stringResource(id = string.lists_my_lists), modifier = Modifier.testTag(TestTag.MyLists))
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
fun MyListsScreenPreview() {
    CineScoutTheme {
        MyListsScreen()
    }
}
