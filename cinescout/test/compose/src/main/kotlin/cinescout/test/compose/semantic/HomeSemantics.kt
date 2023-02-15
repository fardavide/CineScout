package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag

context(ComposeUiTest)
class HomeSemantics {

    fun bottomBar() = onNodeWithTag(TestTag.BottomBar)
    fun drawer() = onNodeWithTag(TestTag.Drawer)
}

context(ComposeUiTest)
fun <T> HomeSemantics(block: context(HomeSemantics) () -> T): T =
    with(HomeSemantics(), block)
