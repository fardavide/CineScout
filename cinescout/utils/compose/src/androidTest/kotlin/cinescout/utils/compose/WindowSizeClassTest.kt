package cinescout.utils.compose

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import kotlin.test.Ignore
import kotlin.test.Test

class WindowSizeClassTest {

    @Test
    @Ignore("For manual test purpose only")
    fun testWindowSizeClass() = runComposeTest {
        setContent {
            Adaptive { windowSizeClass ->
                BoxWithConstraints {
                    val widthWindowSizeClassText = when (windowSizeClass.width) {
                        WindowWidthSizeClass.Compact -> "Compact"
                        WindowWidthSizeClass.Medium -> "Medium"
                        WindowWidthSizeClass.Expanded -> "Expanded"
                    }
                    val heightWindowSizeClassText = when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact -> "Compact"
                        WindowHeightSizeClass.Medium -> "Medium"
                        WindowHeightSizeClass.Expanded -> "Expanded"
                    }
                    val text = "Width: $widthWindowSizeClassText (${maxWidth.value} dpi), " +
                        "Height: $heightWindowSizeClassText (${maxHeight.value} dpi)"
                    Text(modifier = Modifier.testTag("tag"), text = text)
                }
            }
        }
        onNodeWithTag("tag")
            .assertTextEquals("")
    }

}
