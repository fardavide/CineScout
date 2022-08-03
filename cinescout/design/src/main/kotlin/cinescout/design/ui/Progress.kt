package cinescout.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cinescout.design.TestTag

@Composable
fun CenteredProgress(modifier: Modifier = Modifier) {
    Box(modifier = modifier.testTag(TestTag.Progress).fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
