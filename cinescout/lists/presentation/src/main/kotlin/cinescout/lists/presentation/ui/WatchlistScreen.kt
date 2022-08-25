package cinescout.lists.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import studio.forface.cinescout.design.R.string

@Composable
fun WatchlistScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.testTag(TestTag.Watchlist)) {
        Text(text = stringResource(id = string.lists_watchlist))
    }
}

@Composable
@Preview
fun WatchlistScreenPreview() {
    CineScoutTheme {
        WatchlistScreen()
    }
}
