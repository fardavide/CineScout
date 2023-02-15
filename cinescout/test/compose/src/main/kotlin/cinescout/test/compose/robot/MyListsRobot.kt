package cinescout.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import cinescout.test.compose.semantic.HomeSemantics
import cinescout.test.compose.semantic.ListSemantics
import cinescout.test.compose.semantic.MyListsSemantics

context(ComposeUiTest, MyListsSemantics)
class MyListsRobot internal constructor() {

    fun openDisliked(): ListRobot {
        dislikedButton().performClick()
        return ListSemantics { ListRobot() }
    }

    fun openLiked(): ListRobot {
        likedButton().performClick()
        return ListSemantics { ListRobot() }
    }

    fun openRated(): ListRobot {
        ratedButton().performClick()
        return ListSemantics { ListRobot() }
    }

    fun verify(block: Verify.() -> Unit): MyListsRobot {
        HomeSemantics { block(Verify()) }
        return this
    }

    context(ComposeUiTest, MyListsSemantics, HomeSemantics)
    class Verify internal constructor() : HomeRobot.Verify() {

        fun dislikedButtonIsDisplayed() {
            dislikedButton().assertIsDisplayed()
        }

        fun likedButtonIsDisplayed() {
            likedButton().assertIsDisplayed()
        }

        fun ratedButtonIsDisplayed() {
            ratedButton().assertIsDisplayed()
        }

        fun screenIsDisplayed() {
            screen().assertIsDisplayed()
        }

        fun subtitleIsDisplayed() {
            subtitle().assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun MyListsRobot(content: @Composable () -> Unit): MyListsRobot {
    setContent(content)
    return MyListsSemantics { MyListsRobot() }
}
