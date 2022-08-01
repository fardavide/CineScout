package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.stringResource
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.home.presentation.model.HomeState
import coil.compose.AsyncImage
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

@Composable
internal fun HomeDrawer(
    homeState: HomeState,
    drawerState: DrawerState,
    onItemClick: (HomeDrawer.ItemId) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = Modifier.testTag(TestTag.Drawer),
        content = content,
        drawerContent = { HomeDrawerContent(homeState, onItemClick) },
        drawerState = drawerState
    )
}

@Composable
private fun HomeDrawerContent(homeState: HomeState, onItemClick: (HomeDrawer.ItemId) -> Unit) {
    var selectedItemIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (val accountState = homeState.accounts.primary) {
            is HomeState.Accounts.Account.Data -> HomeDrawerItem.Standard(
                icon = {
                    AsyncImage(
                        modifier = Modifier.size(Dimens.Icon.Medium).clip(CircleShape),
                        model = accountState.imageUrl,
                        contentDescription = stringResource(id = string.profile_picture_description),
                        placeholder = painterResource(id = drawable.ic_user)
                    )
                },
                title = TextRes(accountState.username),
                subtitle = TextRes(string.home_manage_accounts),
                onClick = { onItemClick(HomeDrawer.ItemId.Login) }
            )

            else -> HomeDrawerItem.Standard(
                icon = Icons.Rounded.AccountCircle,
                title = TextRes(string.home_login),
                onClick = { onItemClick(HomeDrawer.ItemId.Login) }
            )
        }
        HomeDrawerDivider()
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            title = TextRes(string.coming_soon),
            selected = selectedItemIndex == 0,
            onClick = { selectedItemIndex = 0 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            title = TextRes(string.coming_soon),
            selected = selectedItemIndex == 1,
            onClick = { selectedItemIndex = 1 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            title = TextRes(string.coming_soon),
            selected = selectedItemIndex == 2,
            onClick = { selectedItemIndex = 2 }
        )
        HomeDrawerItem.Selectable(
            icon = Icons.Rounded.Home,
            title = TextRes(string.coming_soon),
            selected = selectedItemIndex == 3,
            onClick = { selectedItemIndex = 3 }
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            when (val appVersionState = homeState.appVersion) {
                is HomeState.AppVersion.Data -> Text(
                    modifier = Modifier.padding(Dimens.Margin.Small),
                    text = stringResource(string.app_version, appVersionState.version),
                    style = MaterialTheme.typography.labelMedium
                )

                HomeState.AppVersion.Loading -> Unit
            }
        }
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
    fun Standard(
        icon: ImageVector,
        title: TextRes,
        subtitle: TextRes? = null,
        onClick: () -> Unit
    ) {
        Selectable(icon = icon, title = title, subtitle = subtitle, selected = false, onClick = onClick)
    }

    @Composable
    fun Standard(
        icon: @Composable () -> Unit,
        title: TextRes,
        subtitle: TextRes? = null,
        onClick: () -> Unit
    ) {
        Selectable(icon = icon, title = title, subtitle = subtitle, selected = false, onClick = onClick)
    }

    @Composable
    fun Selectable(
        icon: ImageVector,
        title: TextRes,
        subtitle: TextRes? = null,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Selectable(
            icon = {
                Icon(
                    modifier = Modifier.size(Dimens.Icon.Medium),
                    imageVector = icon,
                    contentDescription = NoContentDescription
                )
            },
            title = title,
            subtitle = subtitle,
            selected = selected,
            onClick = onClick
        )
    }

    @Composable
    fun Selectable(
        icon: @Composable () -> Unit,
        title: TextRes,
        subtitle: TextRes? = null,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        NavigationDrawerItem(
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon()
                    Spacer(modifier = Modifier.size(Dimens.Margin.Small))
                    Column {
                        Text(
                            text = stringResource(textRes = title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (subtitle != null) {
                            Text(
                                text = stringResource(textRes = subtitle),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
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
    val homeState = HomeState.Loading
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    CineScoutTheme {
        HomeDrawer(homeState = homeState, content = {}, drawerState = drawerState, onItemClick = {})
    }
}