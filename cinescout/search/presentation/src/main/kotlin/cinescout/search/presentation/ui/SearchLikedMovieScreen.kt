package cinescout.search.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.search.presentation.model.SearchLikeMovieAction
import cinescout.search.presentation.model.SearchLikedMovieState
import cinescout.search.presentation.previewdata.SearchLikedMoviePreviewDataProvider
import cinescout.search.presentation.viewmodel.SearchLikedMovieViewModel
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun SearchLikedMovieScreen(modifier: Modifier = Modifier) {
    val viewModel: SearchLikedMovieViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    var searchQuery by remember { mutableStateOf(state.query) }

    SearchLikedMovieScreen(
        state = state.copy(query = searchQuery),
        onQueryChange = {
            viewModel.submit(SearchLikeMovieAction.Search(it))
            searchQuery = it
        },
        modifier = modifier
    )
}

@Composable
fun SearchLikedMovieScreen(
    state: SearchLikedMovieState,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isError = state.result is SearchLikedMovieState.SearchResult.Error

    Column(modifier = modifier.testTag(TestTag.SearchLiked)) {
        Text(
            text = stringResource(id = string.search_liked_movie_for_suggestion),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Medium),
            value = state.query,
            onValueChange = onQueryChange,
            isError = isError,
            label = {
                val id = when {
                    isError -> string.search_liked_movie_no_results
                    else -> string.search_liked_movie_hint
                }
                Text(text = stringResource(id = id))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = NoContentDescription)
            },
            trailingIcon = {
                if (state.result is SearchLikedMovieState.SearchResult.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Dimens.Icon.Medium)
                            .padding(Dimens.Margin.Small)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
            singleLine = true
        )
        if (state.result is SearchLikedMovieState.SearchResult.Data) {
            SearchResults(results = state.result)
        }
    }
}

@Composable
private fun SearchResults(results: SearchLikedMovieState.SearchResult.Data) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, bottom = Dimens.Margin.Small)
    ) {
        LazyColumn {
            items(results.movies, key = { it.movieId.value }) { item ->
                Text(
                    modifier = Modifier.padding( horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small),
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.TABLET)
private fun SearchLikedMovieScreenPreview(
    @PreviewParameter(SearchLikedMoviePreviewDataProvider::class) state: SearchLikedMovieState
) {
    var searchQuery by remember { mutableStateOf(state.query) }
    CineScoutTheme {
        SearchLikedMovieScreen(
            state = state.copy(query = searchQuery),
            onQueryChange = { searchQuery = it }
        )
    }
}
