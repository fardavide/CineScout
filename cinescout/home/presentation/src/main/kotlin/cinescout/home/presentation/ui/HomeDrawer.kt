package cinescout.home.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import studio.forface.cinescout.design.R

@Composable
internal fun HomeDrawer(
    drawerState: DrawerState,
    onItemClick: (HomeDrawer.ItemId) -> Unit,
    content: @Composable() () -> Unit
) {
    ModalNavigationDrawer(
        modifier = Modifier.testTag(TestTag.Drawer),
        content = content,
        drawerContent = { HomeDrawerContent(onItemClick) },
        drawerState = drawerState
    )
}

@Composable
private fun HomeDrawerContent(onItemClick: (HomeDrawer.ItemId) -> Unit) {
    var selectedItemIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HomeDrawerItem.Standard(
            icon = Icons.Rounded.AccountCircle,
            label = R.string.home_login,
            onClick = { onItemClick(HomeDrawer.ItemId.Login) }
        )
        HomeDrawerDivider()
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = R.string.coming_soon,
            selected = selectedItemIndex == 0,
            onClick = { selectedItemIndex = 0 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = R.string.coming_soon,
            selected = selectedItemIndex == 1,
            onClick = { selectedItemIndex = 1 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = R.string.coming_soon,
            selected = selectedItemIndex == 2,
            onClick = { selectedItemIndex = 2 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            label = R.string.coming_soon,
            selected = selectedItemIndex == 3,
            onClick = { selectedItemIndex = 3 }
        )
    }
}

@Composable
private fun HomeDrawerDivider() {
    Divider(
        modifier = Modifier.padding(vertical = Dimens.Margin.XSmall),
        color = MaterialTheme.colorScheme.primaryContainer
    )
}

object HomeDrawer {

    enum class ItemId {
        Login
    }
}

private object HomeDrawerItem {

    @Composable
    fun Standard(icon: ImageVector, @StringRes label: Int, onClick: () -> Unit) {
        Selectable(icon = icon, label = label, selected = false, onClick = onClick)
    }

    @Composable
    fun Selectable(icon: ImageVector, @StringRes label: Int, selected: Boolean, onClick: () -> Unit) {
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(Dimens.Icon.Medium),
                        imageVector = icon,
                        contentDescription = NoContentDescription
                    )
                    Spacer(modifier = Modifier.size(Dimens.Margin.Small))
                    Text(text = stringResource(id = label), style = MaterialTheme.typography.titleMedium)
                }
            },
            selected = selected,
            onClick = onClick
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeDrawerPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    CineScoutTheme {
        HomeDrawer(content = {}, drawerState = drawerState, onItemClick = {})
    }
}
