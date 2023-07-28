package cinescout.lists.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsDropdownChip
import cinescout.design.ui.CsFilterChip
import cinescout.design.util.PreviewUtils
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.SortingDirection
import cinescout.lists.presentation.model.ListFilter
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.sample.GenreSample
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope") // used as receiver
internal fun ListOptions(
    availableGenres: ImmutableList<Genre>,
    config: ListOptions.Config,
    onConfigChange: (ListOptions.Config) -> Unit,
    modifier: Modifier = Modifier
) {
    var isGenreDropdownExpanded by remember { mutableStateOf(false) }
    var isSortingDropdownExpanded by remember { mutableStateOf(false) }
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    BoxWithConstraints {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Small, Alignment.CenterHorizontally)
        ) {
            CsFilterChip(
                selected = config.listFilter == ListFilter.Disliked,
                onClick = { onConfigChange(config.copy(listFilter = ListFilter.Disliked)) },
                label = { Text(text = stringResource(id = string.lists_disliked)) }
            )
            CsFilterChip(
                selected = config.listFilter == ListFilter.Liked,
                onClick = { onConfigChange(config.copy(listFilter = ListFilter.Liked)) },
                label = { Text(text = stringResource(id = string.lists_liked)) }
            )
            CsFilterChip(
                selected = config.listFilter == ListFilter.Rated,
                onClick = { onConfigChange(config.copy(listFilter = ListFilter.Rated)) },
                label = { Text(text = stringResource(id = string.lists_rated)) }
            )
            CsFilterChip(
                selected = config.listFilter == ListFilter.Watchlist,
                onClick = { onConfigChange(config.copy(listFilter = ListFilter.Watchlist)) },
                label = { Text(text = stringResource(id = string.lists_watchlist)) }
            )
            CsFilterChip(
                selected = config.listFilter == ListFilter.InProgress,
                onClick = { onConfigChange(config.copy(listFilter = ListFilter.InProgress)) },
                label = { Text(text = stringResource(id = string.lists_in_progress)) }
            )
            CsDropdownChip(
                onClick = { isGenreDropdownExpanded = true },
                label = {
                    val text = config.genreFilter.fold(
                        ifEmpty = { stringResource(id = string.all_genres) },
                        ifSome = { genre -> genre.name }
                    )
                    Text(text = text)
                }
            )
            CsDropdownChip(
                onClick = { isSortingDropdownExpanded = true },
                label = {
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
            )
            CsDropdownChip(
                onClick = { isTypeDropdownExpanded = true },
                label = {
                    when (config.type) {
                        ScreenplayTypeFilter.All -> Text(text = stringResource(id = string.item_type_all))
                        ScreenplayTypeFilter.Movies -> Text(text = stringResource(id = string.item_type_movies))
                        ScreenplayTypeFilter.TvShows -> Text(text = stringResource(id = string.item_type_tv_shows))
                    }
                }
            )
        }
        GenresDropdownMenu(
            isExpanded = isGenreDropdownExpanded,
            onCollapse = { isGenreDropdownExpanded = false },
            allGenres = availableGenres,
            onGenreChange = { genre -> onConfigChange(config.copy(genreFilter = genre)) },
            selectedGenre = config.genreFilter
        )
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
private fun BoxWithConstraintsScope.GenresDropdownMenu(
    isExpanded: Boolean,
    onCollapse: () -> Unit,
    allGenres: ImmutableList<Genre>,
    onGenreChange: (Option<Genre>) -> Unit,
    selectedGenre: Option<Genre>
) {
    ListOptionsDropdownMenu(
        dropdownItems = allGenres.map { genre ->
            val isSelected = selectedGenre.getOrNull() == genre
            DropdownItem(
                text = TextRes(genre.name),
                isSelected = isSelected,
                onClick = {
                    val newGenreFilter = if (isSelected) none() else genre.some()
                    onGenreChange(newGenreFilter)
                    onCollapse()
                }
            )
        }.toImmutableList(),
        isExpanded = isExpanded,
        onCollapse = onCollapse
    )
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
        val genreFilter: Option<Genre>,
        val listFilter: ListFilter,
        val sorting: ListSorting,
        val type: ScreenplayTypeFilter
    )
}

@Composable
@Preview(backgroundColor = PreviewUtils.WhiteBackgroundColor)
private fun ListOptionsPreview() {
    val config = ListOptions.Config(
        genreFilter = none(),
        listFilter = ListFilter.Watchlist,
        sorting = ListSorting.Rating.Descending,
        type = ScreenplayTypeFilter.All
    )
    CineScoutTheme {
        var currentConfig by remember { mutableStateOf(config) }
        ListOptions(
            availableGenres = persistentListOf(GenreSample.Action, GenreSample.Comedy),
            currentConfig,
            onConfigChange = { currentConfig = it }
        )
    }
}
