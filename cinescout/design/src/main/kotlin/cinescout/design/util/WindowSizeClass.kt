package cinescout.design.util

import android.app.Activity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass as AndroidWindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass as AndroidWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass as AndroidWindowWidthSizeClass

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun Adaptive(content: @Composable (WindowSizeClass) -> Unit) {
    val context = LocalContext.current
    if (context is Activity) {
        val windowSizeClass = WindowSizeClass.fromPlatformValue(calculateWindowSizeClass(context))
        content(windowSizeClass)
    } else {
        BoxWithConstraints {
            val dpSize = DpSize(width = maxWidth, height = maxHeight)
            val windowSizeClass = WindowSizeClass.calculateFromSize(dpSize)
            content(windowSizeClass)
        }
    }
}

data class WindowSizeClass(
    val width: WindowWidthSizeClass,
    val height: WindowHeightSizeClass
) {

    companion object {

        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        fun calculateFromSize(dpSize: DpSize): WindowSizeClass =
            fromPlatformValue(AndroidWindowSizeClass.calculateFromSize(dpSize))

        internal fun fromPlatformValue(value: AndroidWindowSizeClass) = WindowSizeClass(
            width = WindowWidthSizeClass.fromPlatformValue(value.widthSizeClass),
            height = WindowHeightSizeClass.fromPlatformValue(value.heightSizeClass)
        )
    }
}

enum class WindowWidthSizeClass(val platformValue: AndroidWindowWidthSizeClass) {

    Compact(AndroidWindowWidthSizeClass.Compact),
    Medium(AndroidWindowWidthSizeClass.Medium),
    Expanded(AndroidWindowWidthSizeClass.Expanded);

    companion object {

        internal fun fromPlatformValue(value: AndroidWindowWidthSizeClass): WindowWidthSizeClass =
            values().first { it.platformValue == value }
    }
}

enum class WindowHeightSizeClass(val platformValue: AndroidWindowHeightSizeClass) {

    Compact(AndroidWindowHeightSizeClass.Compact),
    Medium(AndroidWindowHeightSizeClass.Medium),
    Expanded(AndroidWindowHeightSizeClass.Expanded);

    companion object {

        internal fun fromPlatformValue(value: AndroidWindowHeightSizeClass): WindowHeightSizeClass =
            values().first { it.platformValue == value }
    }
}
