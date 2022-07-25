package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.testTag(HomeScreen.TestTag), contentAlignment = Alignment.Center) {
        Text(text = "Coming soon", style = MaterialTheme.typography.displaySmall)
    }
}

object HomeScreen {

    const val TestTag = "HomeScreen"
}
