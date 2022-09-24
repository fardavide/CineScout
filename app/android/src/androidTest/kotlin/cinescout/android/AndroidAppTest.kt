package cinescout.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import cinescout.android.testutil.PostNotificationsRule
import cinescout.android.testutil.runComposeAppTest
import cinescout.design.TestTag
import org.junit.Rule
import kotlin.test.Test

class AndroidAppTest {

    @get:Rule
    val permissionsRule = PostNotificationsRule()

    @Test
    fun homeScreenIsShownAtStart() = runComposeAppTest {
        onNodeWithTag(TestTag.Home)
            .assertIsDisplayed()
    }
}
