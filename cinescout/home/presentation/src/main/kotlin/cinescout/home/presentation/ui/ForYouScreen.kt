package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cinescout.design.TestTag
import studio.forface.cinescout.design.R

@Composable
internal fun ForYouScreen() {
    Box(
        modifier = Modifier.testTag(TestTag.ForYou).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.coming_soon), style = MaterialTheme.typography.displaySmall)
    }
}
