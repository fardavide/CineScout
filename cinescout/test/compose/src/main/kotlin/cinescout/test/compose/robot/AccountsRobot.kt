package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cinescout.design.R.string
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.util.getString
import cinescout.test.compose.util.onNodeWithContentDescription
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class AccountsRobot internal constructor() {

    fun close(): HomeRobot {
        onNodeWithContentDescription(string.close_button_description)
            .performClick()
        return HomeSemantics { HomeRobot() }
    }

    fun selectTmdb(): HomeRobot {
        onNodeWithText(string.home_connect_to_tmdb)
            .performClick()
        return HomeSemantics { HomeRobot() }
    }

    fun selectTrakt(): HomeRobot {
        onNodeWithText(string.home_connect_to_trakt)
            .performClick()
        return HomeSemantics { HomeRobot() }
    }

    fun verify(block: Verify.() -> Unit): AccountsRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify {

        fun connectToTmdbIsDisplayed() {
            onNodeWithText(string.home_connect_to_tmdb)
                .assertIsDisplayed()
        }

        fun connectToTraktIsDisplayed() {
            onNodeWithText(string.home_connect_to_trakt)
                .assertIsDisplayed()
        }

        fun tmdbUsernameIsDisplayed(username: String) {
            onNodeWithText(getString(string.home_connected_to_tmdb_as, username))
                .assertIsDisplayed()
        }

        fun traktUsernameIsDisplayed(username: String) {
            onNodeWithText(getString(string.home_connected_to_trakt_as, username))
                .assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun AccountsRobot(content: @Composable () -> Unit): AccountsRobot {
    setContent(content)
    return AccountsRobot()
}
