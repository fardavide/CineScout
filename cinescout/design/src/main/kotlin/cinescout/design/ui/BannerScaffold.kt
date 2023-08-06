package cinescout.design.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cinescout.design.PlainAdaptivePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.NoContentDescription
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
fun BannerScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (WindowSizeClass) -> Unit = {},
    bottomBar: @Composable (WindowSizeClass) -> Unit = {},
    sideRail: @Composable (WindowSizeClass) -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    banner: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color? = null,
    contentColor: Color? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues, WindowSizeClass) -> Unit
) {
    Adaptive { windowSizeClass ->
        val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.background
        val finalContentColor = contentColor ?: contentColorFor(finalContainerColor)

        Row(modifier = modifier) {
            sideRail(windowSizeClass)
            Scaffold(
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
                        topBar(windowSizeClass)
                    }
                },
                bottomBar = { bottomBar(windowSizeClass) },
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = finalContainerColor,
                contentColor = finalContentColor,
                contentWindowInsets = contentWindowInsets,
                content = { paddingValues -> content(paddingValues, windowSizeClass) }
            )
        }
    }
}

@Composable
@PlainAdaptivePreviews
private fun BannerScaffoldPreview() {
    CineScoutTheme {
        BannerScaffold(
            topBar = { CenterAlignedTopAppBar(title = { Text(text = "Title") }) },
            sideRail = { windowSizeClass ->
                if (windowSizeClass.width != WindowWidthSizeClass.Compact) {
                    NavigationRail {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            NavigationRailItem(
                                selected = true,
                                onClick = {},
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Build,
                                        contentDescription = NoContentDescription
                                    )
                                }
                            )
                            NavigationRailItem(
                                selected = false,
                                onClick = {},
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = NoContentDescription
                                    )
                                }
                            )
                            NavigationRailItem(
                                selected = false,
                                onClick = {},
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = NoContentDescription
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues, _ ->
            Text(modifier = Modifier.padding(paddingValues), text = "Content")
        }
    }
}
