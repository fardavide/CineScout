package cinescout.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
fun BannerScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    banner: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color? = null,
    contentColor: Color? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    Adaptive { windowSizeClass ->
        val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.background
        val finalContentColor = contentColor ?: contentColorFor(finalContainerColor)

        Scaffold(
            modifier = modifier,
            topBar = {
                Column {
                    val statusBarHeight = with(LocalDensity.current) {
                        if (windowSizeClass.width == WindowWidthSizeClass.Compact) {
                            WindowInsets.statusBars.getTop(this).toDp()
                        } else {
                            0.dp
                        }
                    }
                    Box(modifier = Modifier.heightIn(min = statusBarHeight)) {
                        banner()
                    }
                    topBar()
                }
            },
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = finalContainerColor,
            contentColor = finalContentColor,
            contentWindowInsets = contentWindowInsets,
            content = content
        )
    }
}
