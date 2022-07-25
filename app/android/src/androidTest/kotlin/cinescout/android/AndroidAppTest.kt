package cinescout.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import cinescout.android.testutil.runComposeAppTest
import cinescout.home.presentation.ui.HomeScreen
import kotlin.test.Test

class AndroidAppTest {

    @Test
    fun homeScreenIsShownAtStart() = runComposeAppTest {
        onNodeWithTag(HomeScreen.TestTag)
            .assertIsDisplayed()
    }
}
