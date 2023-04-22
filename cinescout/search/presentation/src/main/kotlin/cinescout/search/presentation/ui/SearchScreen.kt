package cinescout.search.presentation.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import cinescout.design.AdaptivePreviews
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
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.model.SearchItemUiModel
import cinescout.search.presentation.preview.SearchPreviewDataProvider
import cinescout.search.presentation.state.SearchState
import cinescout.search.presentation.viewmodel.SearchViewModel
import cinescout.utils.compose.paging.PagingItemsState
import com.skydoves.landscapist.coil.CoilImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(actions: SearchScreen.Actions) {
    val viewModel: SearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    SearchScreen(
        state = state,
        search = { viewModel.submit(SearchAction.Search(it)) },
        openItem = actions.toScreenplayDetails
    )
}

@Composable
internal fun SearchScreen(
    state: SearchState,
    search: (String) -> Unit,
    openItem: (ScreenplayIds) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val isError = state.itemsState is PagingItemsState.Error ||
        state.itemsState is PagingItemsState.Empty

    Column(modifier = Modifier.testTag(TestTag.Search), horizontalAlignment = Alignment.CenterHorizontally) {
        var searchQuery by remember { mutableStateOf(state.query) }
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Small),
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                search(newQuery)
            },
            isError = isError,
            label = { Text(text = stringResource(id = string.search_hint)) },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = NoContentDescription)
            },
            trailingIcon = {
                if (state.itemsState.isLoading) {
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
        when (val itemsState = state.itemsState) {
            PagingItemsState.Empty -> SearchErrorText(message = TextRes(string.search_no_results))
            is PagingItemsState.Error -> SearchErrorText(message = itemsState.message)
            PagingItemsState.Loading -> Unit
            is PagingItemsState.NotEmpty -> SearchResults(items = itemsState.items, openItem = openItem)
        }
    }
}

@Composable
private fun SearchResults(items: LazyPagingItems<SearchItemUiModel>, openItem: (ScreenplayIds) -> Unit) {
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
            items(items, key = { it.screenplayIds.uniqueId() }) { item ->
                if (item != null) {
                    Item(item = item, openItem = openItem)
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyItemScope.Item(item: SearchItemUiModel, openItem: (ScreenplayIds) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { openItem(item.screenplayIds) }
            .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.XSmall)
            .animateItemPlacement(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            modifier = Modifier
                .size(width = Dimens.Image.Medium, height = Dimens.Image.Medium)
                .clip(MaterialTheme.shapes.extraSmall)
                .imageBackground(),
            imageModel = { item.screenplayIds.tmdb.asPosterRequest() },
            failure = { FailureImage() },
            loading = { CenteredProgress() }
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Small))
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

object SearchScreen {

    data class Actions(
        val toScreenplayDetails: (ScreenplayIds) -> Unit
    ) {

        companion object {
            
            val Empty = Actions(toScreenplayDetails = {})
        }
    }
}

@Composable
@AdaptivePreviews.WithBackground
private fun SearchScreenPreview(@PreviewParameter(SearchPreviewDataProvider::class) state: SearchState) {
    CineScoutTheme {
        SearchScreen(state = state, search = {}, openItem = {})
    }
}
