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
import org.junit.Rule
import java.io.FileOutputStream
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Manual run only")
class ScreenshotGenerator {

    private val device = "phone"

    @get:Rule
    val appRule = MockAppRule {
        newInstall()
        updatedCache()
    }

    @Test
    fun forYou() {
        appRule {
            newInstall()
            updatedCache()
            forYou {
                movie(SuggestedScreenplaySample.Inception)
            }
        }
        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .awaitMovie(ScreenplaySample.Inception.title)
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("for_you_$device.png")
        }
    }

    @Test
    fun movieDetails() {
        appRule {
            newInstall()
            updatedCache()
            forYou {
                movie(SuggestedScreenplaySample.Inception)
            }
        }
        runComposeAppTest {
            homeRobot
                .openForYou()
                .selectMoviesType()
                .awaitMovie(ScreenplaySample.Inception.title)
                .openMovieDetails()
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("movie_details_$device.png")
        }
    }

    @Test
    fun watchlist() {
        appRule {
            newInstall()
            updatedCache()
            watchlist {
                movie(ScreenplaySample.Inception)
                movie(ScreenplaySample.TheWolfOfWallStreet)
            }
        }
        runComposeAppTest {
            homeRobot
                .openMyLists()
                .awaitMovie(ScreenplaySample.Inception.title)
                .awaitIdle()
                .await(milliseconds = 2_000)

            capture("watchlist_$device.png")
        }
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
