package cinescout.search.presentation.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.NonEmptyList
import cinescout.design.R.drawable
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.search.presentation.model.SearchLikeItemAction
import cinescout.search.presentation.model.SearchLikedItemId
import cinescout.search.presentation.model.SearchLikedItemState
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.model.SearchLikedItemUiModel
import cinescout.search.presentation.model.value
import cinescout.search.presentation.previewdata.SearchLikedItemPreviewDataProvider
import cinescout.search.presentation.viewmodel.SearchLikedItemViewModel
import co.touchlab.kermit.Logger
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
    Logger.v("SearchLikedItemScreen: $state")
    val isError = state.result is SearchLikedItemState.SearchResult.Error ||
        state.result is SearchLikedItemState.SearchResult.NoResults

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
                .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Small),
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
                if (state.result is SearchLikedItemState.SearchResult.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Dimens.Icon.Medium)
                            .padding(Dimens.Margin.Small)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
            singleLine = true
        )
        if (isError) {
            val textRes = when (state.result) {
                is SearchLikedItemState.SearchResult.Error -> state.result.message
                is SearchLikedItemState.SearchResult.NoResults -> {
                    val resultTextRes = when (type) {
                        SearchLikedItemType.Movies -> string.search_liked_movie_no_results
                        SearchLikedItemType.TvShows -> string.search_liked_tv_show_no_results
                    }
                    TextRes(resultTextRes)
                }
                else -> throw IllegalArgumentException("${state.result} is not an error")
            }
            Text(
                modifier = Modifier.padding(Dimens.Margin.Small),
                text = string(textRes = textRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        state.result.items().tap { items ->
            SearchResults(items = items, likeItem = actions.likeItem)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SearchResults(
    items: NonEmptyList<SearchLikedItemUiModel>,
    likeItem: (SearchLikedItemId) -> Unit
) {
    val state = rememberLazyListState()
    LaunchedEffect(items) {
        state.animateScrollToItem(0)
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, bottom = Dimens.Margin.Small)
            .animateContentSize()
    ) {
        LazyColumn(state = state, contentPadding = PaddingValues(vertical = Dimens.Margin.Small)) {
            items(items, key = { it.itemId.value }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable { likeItem(item.itemId) }
                        .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.XSmall)
                        .animateItemPlacement(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoilImage(
                        modifier = Modifier
                            .size(width = Dimens.Image.Medium, height = Dimens.Image.Medium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .imageBackground(),
                        imageModel = { item.posterUrl },
                        failure = {
                            Image(
                                painter = painterResource(id = drawable.ic_warning_30),
                                contentDescription = NoContentDescription
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(Dimens.Margin.Small))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

object SearchLikedItemScreen {

    data class Actions(
        val onQueryChange: (String) -> Unit,
        val likeItem: (SearchLikedItemId) -> Unit
    )
}

@Composable
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
