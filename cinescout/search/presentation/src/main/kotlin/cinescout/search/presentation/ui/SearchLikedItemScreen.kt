package cinescout.search.presentation.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cinescout.CineScoutTestApi
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.FailureImage
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.media.domain.model.asPosterRequest
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.search.presentation.action.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.preview.SearchLikedItemPreviewDataProvider
import cinescout.search.presentation.state.SearchLikedItemState
import cinescout.search.presentation.viewmodel.SearchLikedItemViewModel
import cinescout.utils.compose.paging.PagingItemsState
import com.skydoves.landscapist.coil.CoilImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchLikedItemScreen(type: SearchLikedItemType, modifier: Modifier = Modifier) {
    val viewModel: SearchLikedItemViewModel = koinViewModel()
    LaunchedEffect(type) {
        viewModel.submit(SearchLikeItemAction.SelectItemType(type))
    }
    val state by viewModel.state.collectAsStateLifecycleAware()
    var searchQuery by remember { mutableStateOf(state.query) }

    val actions = SearchLikedItemScreen.Actions(
        onQueryChange = {
            viewModel.submit(SearchLikeItemAction.Search(it))
            searchQuery = it
        },
        likeItem = { viewModel.submit(SearchLikeItemAction.LikeItem(it)) }
    )
    SearchLikedItemScreen(
        state = state.copy(query = searchQuery),
        actions = actions,
        type = type,
        modifier = modifier
    )
}

@Composable
fun SearchLikedItemScreen(
    state: SearchLikedItemState,
    actions: SearchLikedItemScreen.Actions,
    type: SearchLikedItemType,
    modifier: Modifier = Modifier
) {
    val isError = state.itemsState is PagingItemsState.Error ||
        state.itemsState is PagingItemsState.Empty

    Column(modifier = modifier.testTag(TestTag.SearchLiked), horizontalAlignment = Alignment.CenterHorizontally) {
        val promptTextRes = when (type) {
            SearchLikedItemType.Movies -> string.search_liked_movie_prompt
            SearchLikedItemType.TvShows -> string.search_liked_tv_show_prompt
        }
        Text(
            text = stringResource(id = promptTextRes),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.Margin.small, end = Dimens.Margin.small, top = Dimens.Margin.small),
            value = state.query,
            onValueChange = actions.onQueryChange,
            isError = isError,
            label = {
                val searchTextRes = when (type) {
                    SearchLikedItemType.Movies -> string.search_liked_movie_hint
                    SearchLikedItemType.TvShows -> string.search_liked_tv_show_hint
                }
                Text(text = stringResource(id = searchTextRes))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = NoContentDescription)
            },
            trailingIcon = {
                if (state.itemsState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Dimens.Icon.medium)
                            .padding(Dimens.Margin.small)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
            singleLine = true
        )
        when (val itemsState = state.itemsState) {
            PagingItemsState.Empty -> {
                val message = when (type) {
                    SearchLikedItemType.Movies -> string.search_liked_movie_no_results
                    SearchLikedItemType.TvShows -> string.search_liked_tv_show_no_results
                }.let(TextRes::invoke)
                SearchErrorText(message = message)
            }
            is PagingItemsState.Error -> SearchErrorText(message = itemsState.message)
            PagingItemsState.Loading -> Unit
            is PagingItemsState.NotEmpty -> SearchResults(items = itemsState.items, likeItem = actions.likeItem)
        }
    }
}

@Composable
private fun SearchResults(items: LazyPagingItems<SearchLikedItemUiModel>, likeItem: (ScreenplayIds) -> Unit) {
    val state = rememberLazyListState()
    LaunchedEffect(items) {
        state.animateScrollToItem(0)
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.Margin.small, end = Dimens.Margin.small, bottom = Dimens.Margin.small)
            .animateContentSize()
    ) {
        LazyColumn(state = state, contentPadding = PaddingValues(vertical = Dimens.Margin.small)) {
            items(
                count = items.itemCount,
                key = items.itemKey(key = { it.screenplayIds.uniqueId() })
            ) { index ->
                val item = items[index]
                if (item != null) {
                    Item(item = item, likeItem = likeItem)
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.Item(item: SearchLikedItemUiModel, likeItem: (ScreenplayIds) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { likeItem(item.screenplayIds) }
            .padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.xSmall)
            .animateItemPlacement(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            modifier = Modifier
                .size(width = Dimens.Image.medium, height = Dimens.Image.medium)
                .clip(MaterialTheme.shapes.extraSmall)
                .imageBackground(),
            imageModel = { item.screenplayIds.tmdb.asPosterRequest() },
            failure = { FailureImage() },
            loading = { CenteredProgress() }
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.small))
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

object SearchLikedItemScreen {

    data class Actions(
        val onQueryChange: (String) -> Unit,
        val likeItem: (ScreenplayIds) -> Unit
    )
}

@Composable
@CineScoutTestApi
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
private fun SearchLikedItemScreenPreview(
    @PreviewParameter(SearchLikedItemPreviewDataProvider::class) state: SearchLikedItemState
) {
    var searchQuery by remember { mutableStateOf(state.query) }
    CineScoutTheme {
        SearchLikedItemScreen(
            state = state.copy(query = searchQuery),
            actions = SearchLikedItemScreen.Actions(
                onQueryChange = { searchQuery = it },
                likeItem = {}
            ),
            type = SearchLikedItemType.Movies
        )
    }
}
