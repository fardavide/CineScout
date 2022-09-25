package cinescout.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.Adaptive
import cinescout.design.util.WindowWidthSizeClass

@Composable
fun DrawerScaffold(
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color? = null,
    contentColor: Color? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawerGesturesEnabled: Boolean = true,
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    content: @Composable (PaddingValues) -> Unit
) {
    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact -> {
                val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.background
                val finalContentColor = contentColor ?: contentColorFor(finalContainerColor)
                DrawerScaffold.Compact(
                    modifier = modifier,
                    topBar = topBar,
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = finalContainerColor,
                    contentColor = finalContentColor,
                    contentWindowInsets = contentWindowInsets,
                    drawerContent = drawerContent,
                    drawerState = drawerState,
                    drawerGesturesEnabled = drawerGesturesEnabled,
                    drawerScrimColor = drawerScrimColor,
                    content = content
                )
            }
            WindowWidthSizeClass.Medium -> {
                val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.background
                val finalContentColor = contentColor ?: contentColorFor(finalContainerColor)
                DrawerScaffold.Medium(
                    modifier = modifier,
                    topBar = topBar,
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = finalContainerColor,
                    contentColor = finalContentColor,
                    contentWindowInsets = contentWindowInsets,
                    drawerContent = drawerContent,
                    drawerState = drawerState,
                    drawerGesturesEnabled = drawerGesturesEnabled,
                    drawerScrimColor = drawerScrimColor,
                    content = content
                )
            }
            WindowWidthSizeClass.Expanded -> {
                val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.surfaceVariant
                val finalContentColor = contentColor ?: contentColorFor(finalContainerColor)
                DrawerScaffold.Expanded(
                    modifier = modifier,
                    topBar = topBar,
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    containerColor = finalContainerColor,
                    contentColor = finalContentColor,
                    contentWindowInsets = contentWindowInsets,
                    drawerContent = drawerContent,
                    content = content
                )
            }
        }
    }
}

object DrawerScaffold {

    @Composable
    internal fun Compact(
        modifier: Modifier = Modifier,
        drawerContent: @Composable () -> Unit,
        drawerState: DrawerState,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: FabPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        drawerGesturesEnabled: Boolean,
        drawerScrimColor: Color,
        content: @Composable (PaddingValues) -> Unit
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerGesturesEnabled,
            scrimColor = drawerScrimColor,
            drawerContent = {
                DrawerColumn(
                    content = { drawerContent() },
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.extraLarge.copy(
                            topStart = ZeroCornerSize,
                            bottomStart = ZeroCornerSize
                        )
                    )
                )
            }
        ) {
            Scaffold(
                modifier = modifier,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentColor = contentColor,
                contentWindowInsets = contentWindowInsets
            ) { padding ->
                content(padding)
            }
        }
    }

    @Composable
    internal fun Medium(
        modifier: Modifier = Modifier,
        drawerContent: @Composable () -> Unit,
        drawerState: DrawerState,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: FabPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        drawerGesturesEnabled: Boolean,
        drawerScrimColor: Color,
        content: @Composable (PaddingValues) -> Unit
    ) {
        Compact(
            modifier = modifier,
            drawerContent = drawerContent,
            drawerState = drawerState,
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            drawerGesturesEnabled = drawerGesturesEnabled,
            drawerScrimColor = drawerScrimColor,
            content = content
        )
    }

    @Composable
    internal fun Expanded(
        modifier: Modifier = Modifier,
        drawerContent: @Composable () -> Unit,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: FabPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        content: @Composable (PaddingValues) -> Unit
    ) {
        Scaffold(
            modifier = modifier,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            contentColor = contentColor,
            containerColor = containerColor,
            contentWindowInsets = contentWindowInsets
        ) { padding ->
            PermanentNavigationDrawer(drawerContent = {
                DrawerColumn(
                    content = {
                        topBar()
                        Column(Modifier.weight(1f)) { drawerContent() }
                        bottomBar()
                    },
                    width = Dimens.Component.XXXLarge
                )
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.extraLarge)
                ) {
                    Box(modifier = Modifier.padding(Dimens.Margin.Small)) {
                        content(padding)
                    }
                }
            }
        }
    }

    @Composable
    private fun DrawerColumn(
        content: @Composable ColumnScope.() -> Unit,
        modifier: Modifier = Modifier,
        width: Dp = DrawerDefaults.MaximumDrawerWidth
    ) {
        Column(
            modifier = modifier
                .width(width)
                .fillMaxHeight()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            content()
        }
    }
}

@Composable
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
private fun DrawerScaffoldPreview() {
    CineScoutTheme {
        DrawerScaffold(
            drawerContent = {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) { Text(text = "Drawer") }
            },
            drawerState = rememberDrawerState(DrawerValue.Open),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text(text = "Content") }
            }
        )
    }
}
