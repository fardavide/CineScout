package cinescout.profile.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cinescout.design.AdaptivePreviews
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.testTag(TestTag.Profile),
        text = "Profile"
    )
}

@Composable
@AdaptivePreviews.Plain
private fun ProfileScreenPreview() {
    CineScoutTheme {
        ProfileScreen()
    }
}
