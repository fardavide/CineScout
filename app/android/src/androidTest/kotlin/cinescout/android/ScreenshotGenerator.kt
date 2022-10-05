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

        capture("for_you.png")
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
