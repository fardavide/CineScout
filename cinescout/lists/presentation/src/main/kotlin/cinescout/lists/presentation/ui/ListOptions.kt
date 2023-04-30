package cinescout.lists.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.SortingDirection
import cinescout.lists.presentation.model.ListFilter
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun ListOptions(
    config: ListOptions.Config,
    onConfigChange: (ListOptions.Config) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSortingDropdownExpanded by remember { mutableStateOf(false) }
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    BoxWithConstraints {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Small, Alignment.CenterHorizontally)
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
                onClick = { isSortingDropdownExpanded = true },
                label = {
                    Row {
                        when (config.sorting) {
                            is ListSorting.Rating -> Text(text = stringResource(id = string.lists_sorting_rating))
                            is ListSorting.ReleaseDate -> Text(text = stringResource(id = string.lists_sorting_release))
                        }
                        Spacer(modifier = Modifier.width(Dimens.Margin.XSmall))
                        Text(
                            text = when (config.sorting.direction) {
                                SortingDirection.Ascending -> "⬆️"
                                SortingDirection.Descending -> "⬇️"
                            }
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = NoContentDescription
                    )
                }
            )
            ElevatedSuggestionChip(
                onClick = { isTypeDropdownExpanded = true },
                label = {
                    Row {
                        when (config.type) {
                            ScreenplayTypeFilter.All -> Text(text = stringResource(id = string.item_type_all))
                            ScreenplayTypeFilter.Movies -> Text(text = stringResource(id = string.item_type_movies))
                            ScreenplayTypeFilter.TvShows -> Text(text = stringResource(id = string.item_type_tv_shows))
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = NoContentDescription
                    )
                }
            )
        }
        SortingDropdownMenu(
            isExpanded = isSortingDropdownExpanded,
            onCollapse = { isSortingDropdownExpanded = false },
            onSortingChange = { sorting -> onConfigChange(config.copy(sorting = sorting)) },
            sorting = config.sorting
        )
        TypeDropdownMenu(
            isExpanded = isTypeDropdownExpanded,
            onCollapse = { isTypeDropdownExpanded = false },
            onTypeChange = { screenplayType -> onConfigChange(config.copy(type = screenplayType)) },
            type = config.type
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.SortingDropdownMenu(
    isExpanded: Boolean,
    onCollapse: () -> Unit,
    onSortingChange: (ListSorting) -> Unit,
    sorting: ListSorting
) {
    ListOptionsDropdownMenu(
        dropdownItems = persistentListOf(
            DropdownItem(
                text = TextRes(string.lists_sorting_rating),
                isSelected = false,
                onClick = {
                    val newSorting = when (sorting) {
                        ListSorting.Rating.Descending -> ListSorting.Rating.Ascending
                        ListSorting.Rating.Ascending -> ListSorting.Rating.Descending
                        else -> ListSorting.Rating.Descending
                    }
                    onSortingChange(newSorting)
                    onCollapse()
                }
            ),
            DropdownItem(
                text = TextRes(string.lists_sorting_release),
                isSelected = false,
                onClick = {
                    val newSorting = when (sorting) {
                        ListSorting.ReleaseDate.Descending -> ListSorting.ReleaseDate.Ascending
                        ListSorting.ReleaseDate.Ascending -> ListSorting.ReleaseDate.Descending
                        else -> ListSorting.ReleaseDate.Descending
                    }
                    onSortingChange(newSorting)
                    onCollapse()
                }
            )
        ),
        isExpanded = isExpanded,
        onCollapse = onCollapse
    )
}

@Composable
private fun BoxWithConstraintsScope.TypeDropdownMenu(
    isExpanded: Boolean,
    onCollapse: () -> Unit,
    onTypeChange: (ScreenplayTypeFilter) -> Unit,
    type: ScreenplayTypeFilter
) {
    ListOptionsDropdownMenu(
        dropdownItems = persistentListOf(
            DropdownItem(
                text = TextRes(string.item_type_all),
                isSelected = type == ScreenplayTypeFilter.All,
                onClick = {
                    onTypeChange(ScreenplayTypeFilter.All)
                    onCollapse()
                }
            ),
            DropdownItem(
                text = TextRes(string.item_type_movies),
                isSelected = type == ScreenplayTypeFilter.Movies,
                onClick = {
                    onTypeChange(ScreenplayTypeFilter.Movies)
                    onCollapse()
                }
            ),
            DropdownItem(
                text = TextRes(string.item_type_tv_shows),
                isSelected = type == ScreenplayTypeFilter.TvShows,
                onClick = {
                    onTypeChange(ScreenplayTypeFilter.TvShows)
                    onCollapse()
                }
            )
        ),
        isExpanded = isExpanded,
        onCollapse = onCollapse
    )
}

@Composable
private fun BoxWithConstraintsScope.ListOptionsDropdownMenu(
    dropdownItems: ImmutableList<DropdownItem>,
    isExpanded: Boolean,
    onCollapse: () -> Unit
) {
    val dropdownWidthFraction = 0.45f
    val dropdownX = maxWidth * dropdownWidthFraction - Dimens.Margin.Medium
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(dropdownWidthFraction),
        expanded = isExpanded,
        onDismissRequest = onCollapse,
        offset = DpOffset(x = dropdownX, y = 0.dp)
    ) {
        for (item in dropdownItems) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.dropdownItemBackground(isSelected = item.isSelected),
                        text = string(textRes = item.text)
                    )
                },
                onClick = {
                    item.onClick()
                    onCollapse()
                }
            )
        }
    }
}

private data class DropdownItem(
    val text: TextRes,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

private fun Modifier.dropdownItemBackground(isSelected: Boolean) = composed {
    background(
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        shape = MaterialTheme.shapes.small
    ).padding(Dimens.Margin.Small)
}

internal object ListOptions {

    data class Config(
        val filter: ListFilter,
        val sorting: ListSorting,
        val type: ScreenplayTypeFilter
    )
}

@Composable
@Preview(showSystemUi = true)
private fun ListOptionsPreview() {
    val config = ListOptions.Config(
        filter = ListFilter.Watchlist,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    CineScoutTheme {
        var currentConfig by remember { mutableStateOf(config) }
        ListOptions(currentConfig, onConfigChange = { currentConfig = it })
    }
}
