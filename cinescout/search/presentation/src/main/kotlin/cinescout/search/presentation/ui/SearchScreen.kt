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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cinescout.CineScoutTestApi
import cinescout.design.TestTag
import cinescout.design.WithBackgroundAdaptivePreviews
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
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.presentation.ui.ScreenplayTypeBadge
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
        var searchQuery by remember {
            mutableStateOf(
                TextFieldValue(
                    text = state.query,
                    selection = TextRange(state.query.length)
                )
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(start = Dimens.Margin.small, end = Dimens.Margin.small, top = Dimens.Margin.small),
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                search(newQuery.text)
            },
            isError = isError,
            label = { Text(text = stringResource(id = string.search_hint)) },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = NoContentDescription)
            },
            trailingIcon = {
                when (state.searchFieldIcon) {
                    SearchState.SearchFieldIcon.None -> Unit
                    SearchState.SearchFieldIcon.Clear -> Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = string.close_button_description),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { searchQuery = TextFieldValue(text = "") }
                    )
                    SearchState.SearchFieldIcon.Loading -> CircularProgressIndicator(
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
                    Item(item = item, openItem = openItem)
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.Item(item: SearchItemUiModel, openItem: (ScreenplayIds) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { openItem(item.screenplayIds) }
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
        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            ScreenplayTypeBadge(type = ScreenplayType.from(item.screenplayIds))
        }
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
@CineScoutTestApi
@WithBackgroundAdaptivePreviews
private fun SearchScreenPreview(@PreviewParameter(SearchPreviewDataProvider::class) state: SearchState) {
    CineScoutTheme {
        SearchScreen(state = state, search = {}, openItem = {})
    }
}
