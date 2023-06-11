package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.frameColor
import cinescout.details.presentation.model.DetailsActionsUiModel
import cinescout.details.presentation.previewdata.DetailActionsUiModelPreviewData
import cinescout.resources.R.string

@Composable
internal fun DetailsBottomBar(uiModel: DetailsActionsUiModel, actions: DetailsActionBar.Actions) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.frameColor,
        actions = {
            DetailsActionBarItem(uiModel = uiModel.actionItemUiModel, onClick = actions.openAddToHistory)
            DetailsActionBarItem(uiModel = uiModel.personalRatingUiModel, onClick = actions.openRating)
            DetailsActionBarItem(uiModel = uiModel.watchlistUiModel, onClick = actions.toggleWatchlist)
        },
        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                onClick = actions.openEdit
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(id = string.details_edit_info)
                )
            }
        }
    )
}

@Composable
internal fun DetailsSideBar(uiModel: DetailsActionsUiModel, actions: DetailsActionBar.Actions) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.frameColor,
        header = {
            IconButton(onClick = actions.back) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = string.back_button_description)
                )
            }
        }
    ) {
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            onClick = actions.openEdit
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = stringResource(id = string.details_edit_info))
        }
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxHeight(0.6f), verticalArrangement = Arrangement.SpaceEvenly) {
                DetailsActionBarItem(uiModel = uiModel.actionItemUiModel, onClick = actions.openAddToHistory)
                DetailsActionBarItem(uiModel = uiModel.personalRatingUiModel, onClick = actions.openRating)
                DetailsActionBarItem(uiModel = uiModel.watchlistUiModel, onClick = actions.toggleWatchlist)
            }
        }
    }
}

object DetailsActionBar {

    data class Actions(
        val openAddToHistory: () -> Unit,
        val back: () -> Unit,
        val openEdit: () -> Unit,
        val openRating: () -> Unit,
        val toggleWatchlist: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                openAddToHistory = {},
                back = {},
                openEdit = {},
                openRating = {},
                toggleWatchlist = {}
            )
        }
    }
}

@Preview
@Composable
private fun DetailsBottomBarPreview(
    @PreviewParameter(DetailActionsUiModelPreviewData::class) uiModel: DetailsActionsUiModel
) {
    CineScoutTheme {
        DetailsBottomBar(
            uiModel = uiModel,
            actions = DetailsActionBar.Actions.Empty
        )
    }
}

@Preview
@Composable
private fun DetailsSideBarPreview(
    @PreviewParameter(DetailActionsUiModelPreviewData::class) uiModel: DetailsActionsUiModel
) {
    CineScoutTheme {
        DetailsSideBar(
            uiModel = uiModel,
            actions = DetailsActionBar.Actions.Empty
        )
    }
}
