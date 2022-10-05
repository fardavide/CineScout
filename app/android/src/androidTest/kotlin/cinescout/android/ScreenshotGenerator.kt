package cinescout.android

import android.graphics.Bitmap
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.screenshot.Screenshot
import cinescout.android.testutil.ComposeAppTest
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.test.mock.MockAppRule
import org.junit.Rule
import java.io.FileOutputStream
import kotlin.test.Test

class ScreenshotGenerator {

    private val device = "tablet"

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
        updatedCache()
        disableForYouHint()
        forYou {
            item(MovieTestData.Inception)
        }
    }

    @Test
    fun forYou() = runComposeAppTest {
        homeRobot
            .asForYou()
            .awaitIdle()

        capture("for_you_$device.png")
    }

    @Test
    fun forYouAction() = runComposeAppTest {
        homeRobot
            .asForYou()
            .awaitIdle()
            .performLikeAction(fraction = 0.5f, performUp = false)

        capture("for_you_action_$device.png")
    }

    @Test
    fun movieDetails() = runComposeAppTest {
        homeRobot
            .asForYou()
            .openDetails()
            .awaitIdle()

        capture("movie_details_$device.png")
    }

    @Test
    fun watchlist() = runComposeAppTest {
        homeRobot
            .openDrawer()
            .openWatchlist()
            .awaitIdle()

        capture("watchlist_$device.png")
    }
}

private fun ComposeAppTest.capture(name: String) {
    waitForIdle()

    val compressFormat = Bitmap.CompressFormat.PNG
    val bitmap = Screenshot
        .capture()
        .setName(name)
        .setFormat(compressFormat)
        .bitmap

    val path = InstrumentationRegistry.getInstrumentation().targetContext.filesDir.canonicalPath
    FileOutputStream("$path/$name").use { out ->
        bitmap.compress(compressFormat, 100, out)
    }

    onRoot()

    // adb shell 'ls /data/data/studio.forface.cinescout2/files/*png' | tr -d '\r' | xargs -n1 adb pull
}
