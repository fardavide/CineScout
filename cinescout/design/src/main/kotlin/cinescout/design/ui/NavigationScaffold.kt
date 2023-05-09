package cinescout.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import cinescout.design.AdaptivePreviews
import cinescout.design.model.NavigationItem
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.NoContentDescription
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.TextRes
import cinescout.resources.image
import cinescout.resources.string
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowWidthSizeClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NavigationScaffold(
    items: ImmutableList<NavigationItem>,
    modifier: Modifier = Modifier,
    banner: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val params = NavigationScaffold.Params(
        banner = banner,
        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
        content = content,
        items = items,
        modifier = modifier,
        snackbarHost = snackbarHost,
        topBar = topBar
    )
    Adaptive { windowSizeClass ->
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact -> NavigationScaffold.Compact(params)
            WindowWidthSizeClass.Medium -> NavigationScaffold.Medium(params)
            WindowWidthSizeClass.Expanded -> NavigationScaffold.Expanded(params)
        }
    }
}

object NavigationScaffold {

    @Composable
    internal fun Compact(params: Params) {
        BannerScaffold(
            modifier = params.modifier,
            banner = params.banner,
            bottomBar = {
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    for (item in params.items) {
                        NavigationBarItem(
                            selected = item.isSelected,
                            onClick = item.onClick,
                            icon = {
                                NavigationIcon(
                                    icon = item.icon,
                                    selectedIcon = item.selectedIcon,
                                    isSelected = item.isSelected
                                )
                            },
                            label = { Text(text = string(textRes = item.label)) }
                        )
                    }
                }
            },
            containerColor = params.containerColor,
            snackbarHost = params.snackbarHost,
            topBar = params.topBar
        ) { paddingValues -> ContentContainer(paddingValues = paddingValues, content = params.content) }
    }

    @Composable
    internal fun Medium(params: Params) {
        Compact(params)
    }

    @Composable
    internal fun Expanded(params: Params) {
        Row(modifier = params.modifier) {
            NavigationRail(
                containerColor = params.containerColor
            ) {
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    for (item in params.items) {
                        NavigationRailItem(
                            selected = item.isSelected,
                            onClick = item.onClick,
                            icon = {
                                NavigationIcon(
                                    icon = item.icon,
                                    selectedIcon = item.selectedIcon,
                                    isSelected = item.isSelected
                                )
                            },
                            label = { Text(text = string(textRes = item.label)) }
                        )
                    }
                }
            }
            BannerScaffold(
                banner = params.banner,
                containerColor = params.containerColor,
                snackbarHost = params.snackbarHost,
                topBar = params.topBar
            ) { paddingValues -> ContentContainer(paddingValues = paddingValues, content = params.content) }
        }
    }

    @Composable
    private fun ContentContainer(paddingValues: PaddingValues, content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
        ) {
            content()
        }
    }

    @Composable
    private fun NavigationIcon(
        icon: ImageRes,
        selectedIcon: ImageRes,
        isSelected: Boolean
    ) {
        Icon(
            painter = image(imageRes = if (isSelected) selectedIcon else icon),
            contentDescription = NoContentDescription
        )
    }

    internal data class Params(
        val banner: @Composable () -> Unit,
        val content: @Composable () -> Unit,
        val containerColor: Color,
        val items: List<NavigationItem>,
        val modifier: Modifier,
        val snackbarHost: @Composable () -> Unit,
        val topBar: @Composable () -> Unit
    )
}

@Composable
@AdaptivePreviews.Plain
private fun NavigationScaffoldPreview() {
    val items = persistentListOf(
        NavigationItem(
            label = TextRes("For You"),
            icon = ImageRes(drawable.ic_magic_wand),
            selectedIcon = ImageRes(drawable.ic_magic_wand_filled),
            isSelected = true,
            onClick = {}
        ),
        NavigationItem(
            label = TextRes("List"),
            icon = ImageRes(drawable.ic_list),
            selectedIcon = ImageRes(drawable.ic_list_filled),
            isSelected = false,
            onClick = {}
        ),
        NavigationItem(
            label = TextRes("Bookmark"),
            icon = ImageRes(drawable.ic_bookmark),
            selectedIcon = ImageRes(drawable.ic_bookmark_filled),
            isSelected = false,
            onClick = {}
        )
    )
    CineScoutTheme {
        NavigationScaffold(
            items = items,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                    title = { Text(text = "Top bar") }
                )
            }
        ) { Text(text = "Content") }
    }
}
