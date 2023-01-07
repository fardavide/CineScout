package cinescout.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.ImageRes
import cinescout.design.TextRes
import cinescout.design.image
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.DrawerScaffold
import cinescout.design.util.NoContentDescription
import cinescout.home.presentation.model.HomeState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

@Composable
internal fun HomeDrawerContent(homeState: HomeState, onItemClick: (HomeDrawer.ItemId) -> Unit) {
    var selectedItemId by rememberSaveable { mutableStateOf(HomeDrawer.ItemId.ForYou) }
    when (val accountState = homeState.accounts.primary) {
        is HomeState.Accounts.Account.Data -> HomeDrawerItem.Standard(
            icon = {
                CoilImage(
                    modifier = Modifier
                        .size(Dimens.Icon.Medium)
                        .clip(CircleShape),
                    imageModel = { accountState.imageUrl },
                    imageOptions = ImageOptions(
                        contentDescription = stringResource(id = string.profile_picture_description)
                    ),
                    failure = {
                        Image(
                            painter = painterResource(id = drawable.ic_user_color),
                            contentDescription = NoContentDescription
                        )
                    }
                )
            },
            title = TextRes(accountState.username),
            subtitle = TextRes(string.home_manage_accounts),
            onClick = { onItemClick(HomeDrawer.ItemId.Login) }
        )

        else -> HomeDrawerItem.Standard(
            icon = ImageRes(Icons.Rounded.AccountCircle),
            title = TextRes(string.home_login),
            onClick = { onItemClick(HomeDrawer.ItemId.Login) }
        )
    }
    HomeDrawerDivider()
    HomeDrawerItem.Selectable(
        icon = ImageRes(drawable.ic_magic_wand),
        title = TextRes(string.suggestions_for_you),
        selected = selectedItemId == HomeDrawer.ItemId.ForYou,
        onClick = {
            selectedItemId = HomeDrawer.ItemId.ForYou
            onItemClick(HomeDrawer.ItemId.ForYou)
        }
    )
    HomeDrawerItem.Selectable(
        icon = ImageRes(drawable.ic_bookmark),
        title = TextRes(string.lists_watchlist),
        selected = selectedItemId == HomeDrawer.ItemId.Watchlist,
        onClick = {
            selectedItemId = HomeDrawer.ItemId.Watchlist
            onItemClick(HomeDrawer.ItemId.Watchlist)
        }
    )
    HomeDrawerItem.Selectable(
        icon = ImageRes(drawable.ic_list),
        title = TextRes(string.lists_my_lists),
        selected = selectedItemId == HomeDrawer.ItemId.MyLists,
        onClick = {
            selectedItemId = HomeDrawer.ItemId.MyLists
            onItemClick(HomeDrawer.ItemId.MyLists)
        }
    )
    HomeDrawerItem.Standard(
        icon = ImageRes(Icons.Rounded.Home),
        title = TextRes(string.coming_soon),
        onClick = {}
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

@Composable
private fun HomeDrawerDivider() {
    Divider(
        modifier = Modifier.padding(vertical = Dimens.Margin.XSmall),
        color = MaterialTheme.colorScheme.primaryContainer
    )
}

object HomeDrawer {

    enum class ItemId {
        ForYou,
        MyLists,
        Login,
        Watchlist
    }
}

private object HomeDrawerItem {

    @Composable
    fun Standard(
        icon: ImageRes,
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
        icon: ImageRes,
        title: TextRes,
        subtitle: TextRes? = null,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Selectable(
            icon = {
                Icon(
                    modifier = Modifier.size(Dimens.Icon.Small),
                    painter = image(icon),
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
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
            ),
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon()
                    Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
                    Column {
                        Text(
                            text = string(textRes = title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (subtitle != null) {
                            Text(
                                text = string(textRes = subtitle),
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
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
private fun HomeDrawerPreview() {
    val homeState = HomeState.Loading
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    CineScoutTheme {
        DrawerScaffold(
            drawerContent = { HomeDrawerContent(homeState = homeState, onItemClick = {}) },
            drawerState = drawerState,
            content = { CenteredErrorText(text = TextRes("content")) }
        )
    }
}
