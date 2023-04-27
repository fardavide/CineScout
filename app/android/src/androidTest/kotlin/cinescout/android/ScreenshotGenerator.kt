@file:Suppress("DEPRECATION")

package cinescout.android

import android.graphics.Bitmap
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.screenshot.Screenshot
import cinescout.android.testutil.ComposeAppTest
import cinescout.android.testutil.homeRobot
import cinescout.android.testutil.runComposeAppTest
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import cinescout.test.compose.util.await
import cinescout.test.mock.junit4.MockAppRule
import org.junit.Ignore
import org.junit.Rule
import java.io.FileOutputStream
import kotlin.test.Test

@Ignore("Manual run only")
class ScreenshotGenerator {

    private val device = Device.Phone

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
    }

    @Test
    fun forYou() {
        appRule {
            newInstall()
            forYou {
                movie(SuggestedScreenplaySample.Inception)
            }
        }
        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .awaitScreenplay(ScreenplaySample.Inception.title)
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("for_you_$device.png")
        }
    }

    @Test
    fun details() {
        appRule {
            newInstall()
            forYou {
                movie(SuggestedScreenplaySample.Inception)
            }
        }
        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .awaitScreenplay(ScreenplaySample.Inception.title)
                .openDetails()
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("details_$device.png")
        }
    }

    @Test
    fun lists() {
        appRule {
            newInstall()
            watchlist {
                add(ScreenplaySample.Inception)
                add(ScreenplaySample.BreakingBad)
                add(ScreenplaySample.Grimm)
            }
        }
        runComposeAppTest {
            homeRobot
                .openMyLists()
                .awaitScreenplay(ScreenplaySample.Inception.title)
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("lists_$device.png")
        }
    }

    @Test
    fun search() {
        appRule {
            newInstall()
            cached {
                add(ScreenplaySample.BreakingBad)
                add(ScreenplaySample.Dexter)
                add(ScreenplaySample.Grimm)
                add(ScreenplaySample.Inception)
                add(ScreenplaySample.TheWolfOfWallStreet)
                add(ScreenplaySample.War)
            }
        }
        runComposeAppTest {
            homeRobot
                .openSearch()
                .awaitScreenplay(ScreenplaySample.BreakingBad.title)
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("search_$device.png")
        }
    }
}

@Suppress("unused")
private enum class Device(val string: String) {
    Foldable("fold"),
    Phone("phone"),
    Tablet("tablet");

    override fun toString() = string
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
