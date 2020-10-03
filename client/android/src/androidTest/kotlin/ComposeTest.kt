import androidx.ui.test.AndroidComposeTestRule
import androidx.ui.test.ComposeTestRule
import androidx.ui.test.ComposeTestRuleJUnit
import androidx.ui.test.createAndroidComposeRule
import client.android.theme.CineScoutTheme
import client.android.ui.MovieDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.declare
import studio.forface.cinescout.MainActivity
import util.DispatchersProvider
import util.test.CoroutinesTest

abstract class ComposeTest : KoinTest, CoroutinesTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    protected abstract fun TestScope.init()

    protected fun composeTest(block: suspend TestScope.() -> Unit) = try {
        coroutinesTest {
            loadKoinModules(
                module(override = true) {
                    single { dispatchers }
                }
            )

            TestScope(composeTestRule, this).run {
                init()
                block()
            }
        }
    } catch (e: IllegalStateException) {
        if (e.message != "This job has not completed yet") throw e
        Unit
    }


}

class TestScope(composeTestRule: ComposeTestRuleJUnit, coroutineScope: TestCoroutineScope):
    ComposeTestRuleJUnit by composeTestRule,
    TestCoroutineScope by coroutineScope
