package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ItemsListSemantics
import cinescout.test.compose.util.awaitDisplayed

context(ComposeUiTest, ItemsListSemantics)
class ItemsListRobot internal constructor() {

    fun awaitIdle(): ItemsListRobot {
        waitForIdle()
        return this
    }

    fun awaitMovie(title: String): ItemsListRobot {
        title(title).awaitDisplayed()
        return this
    }

    fun openScreenplay(title: String): ScreenplayDetailsRobot {
        title(title).awaitDisplayed().performClick()
        return ScreenplayDetailsRobot()
    }

    fun verify(block: Verify.() -> Unit): ItemsListRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, ItemsListSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun titleIsDisplayed() {
            title().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun ItemsListRobot(content: @Composable () -> Unit): ItemsListRobot {
    setContent(content)
    return ItemsListSemantics { ItemsListRobot() }
}
