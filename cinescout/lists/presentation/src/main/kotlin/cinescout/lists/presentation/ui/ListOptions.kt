package cinescout.lists.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.R.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListType

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun ListOptions(
    config: ListOptions.Config,
    onConfigChange: (ListOptions.Config) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    Box {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.XSmall, Alignment.CenterHorizontally)
        ) {
            ElevatedFilterChip(
                selected = config.filter == ListFilter.Disliked,
                onClick = { onConfigChange(config.copy(filter = ListFilter.Disliked)) },
                label = { Text(text = stringResource(id = string.lists_disliked)) }
            )
            ElevatedFilterChip(
                selected = config.filter == ListFilter.Liked,
                onClick = { onConfigChange(config.copy(filter = ListFilter.Liked)) },
                label = { Text(text = stringResource(id = string.lists_liked)) }
            )
            ElevatedFilterChip(
                selected = config.filter == ListFilter.Rated,
                onClick = { onConfigChange(config.copy(filter = ListFilter.Rated)) },
                label = { Text(text = stringResource(id = string.lists_rated)) }
            )
            ElevatedFilterChip(
                selected = config.filter == ListFilter.Watchlist,
                onClick = { onConfigChange(config.copy(filter = ListFilter.Watchlist)) },
                label = { Text(text = stringResource(id = string.lists_watchlist)) }
            )
            ElevatedSuggestionChip(
                onClick = { isDropdownExpanded = true },
                label = {
                    Row {
                        when (config.type) {
                            ListType.All -> Text(text = stringResource(id = string.item_type_all))
                            ListType.Movies -> Text(text = stringResource(id = string.item_type_movies))
                            ListType.TvShows -> Text(text = stringResource(id = string.item_type_tv_shows))
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = NoContentDescription
                    )
                }
            )
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.TopEnd),
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.dropdownItemBackground(isSelected = config.type == ListType.All),
                        text = stringResource(id = string.item_type_all)
                    )
                },
                onClick = {
                    onConfigChange(config.copy(type = ListType.All))
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.dropdownItemBackground(isSelected = config.type == ListType.Movies),
                        text = stringResource(id = string.item_type_movies)
                    )
                },
                onClick = {
                    onConfigChange(config.copy(type = ListType.Movies))
                    isDropdownExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.dropdownItemBackground(isSelected = config.type == ListType.TvShows),
                        text = stringResource(id = string.item_type_tv_shows)
                    )
                },
                onClick = {
                    onConfigChange(config.copy(type = ListType.TvShows))
                    isDropdownExpanded = false
                }
            )
        }
    }
}

private fun Modifier.dropdownItemBackground(isSelected: Boolean) = composed {
    background(
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        shape = MaterialTheme.shapes.small
    ).padding(Dimens.Margin.Small)
}

internal object ListOptions {

    data class Config(
        val filter: ListFilter,
        val type: ListType
    )
}

@Composable
@Preview(showSystemUi = true, backgroundColor = 0xFFFFFFFF)
private fun ListOptionsPreview() {
    val config = ListOptions.Config(
        filter = ListFilter.Watchlist,
        type = ListType.All
    )
    CineScoutTheme {
        var currentConfig by remember { mutableStateOf(config) }
        ListOptions(currentConfig, onConfigChange = { currentConfig = it })
    }
}
