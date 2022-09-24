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
import androidx.compose.runtime.rememberCoroutineScope
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
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.search.presentation.model.SearchLikeMovieAction
import cinescout.search.presentation.model.SearchLikedMovieState
import cinescout.search.presentation.model.SearchLikedMovieUiModel
import cinescout.search.presentation.previewdata.SearchLikedMoviePreviewDataProvider
import cinescout.search.presentation.viewmodel.SearchLikedMovieViewModel
import co.touchlab.kermit.Logger
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R
import studio.forface.cinescout.design.R.string

@Composable
fun SearchLikedMovieScreen(modifier: Modifier = Modifier) {
    val viewModel: SearchLikedMovieViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    var searchQuery by remember { mutableStateOf(state.query) }

    val actions = SearchLikedMovieScreen.Actions(
        onQueryChange = {
            viewModel.submit(SearchLikeMovieAction.Search(it))
            searchQuery = it
        },
        likeMovie = { viewModel.submit(SearchLikeMovieAction.LikeMovie(it)) }
    )
    SearchLikedMovieScreen(
        state = state.copy(query = searchQuery),
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun SearchLikedMovieScreen(
    state: SearchLikedMovieState,
    actions: SearchLikedMovieScreen.Actions,
    modifier: Modifier = Modifier
) {
    Logger.v("SearchLikedMovieScreen: $state")
    val isError = state.result is SearchLikedMovieState.SearchResult.Error ||
        state.result is SearchLikedMovieState.SearchResult.NoResults

    Column(modifier = modifier.testTag(TestTag.SearchLiked), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = string.search_liked_movie_prompt),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Small),
            value = state.query,
            onValueChange = actions.onQueryChange,
            isError = isError,
            label = { Text(text = stringResource(id = string.search_liked_movie_hint)) },
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
        if (isError) {
            val textRes = when (state.result) {
                is SearchLikedMovieState.SearchResult.Error -> state.result.message
                is SearchLikedMovieState.SearchResult.NoResults -> TextRes(string.search_liked_movie_no_results)
                else -> throw IllegalArgumentException("${state.result} is not an error")
            }
            Text(
                modifier = Modifier.padding(Dimens.Margin.Small),
                text = string(textRes = textRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        state.result.movies().tap { movies ->
            SearchResults(movies = movies, likeMovie = actions.likeMovie)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SearchResults(movies: NonEmptyList<SearchLikedMovieUiModel>, likeMovie: (TmdbMovieId) -> Unit) {
    val state = rememberLazyListState()
    rememberCoroutineScope().launch {
        state.animateScrollToItem(0)
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, bottom = Dimens.Margin.Small)
            .animateContentSize()
    ) {
        LazyColumn(state = state, contentPadding = PaddingValues(vertical = Dimens.Margin.Small)) {
            items(movies, key = { it.movieId.value }) { movie ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable { likeMovie(movie.movieId) }
                        .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.XSmall)
                        .animateItemPlacement(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        modifier = Modifier
                            .size(width = Dimens.Image.Medium, height = Dimens.Image.Medium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .imageBackground(),
                        imageModel = movie.posterUrl,
                        failure = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_warning_30),
                                contentDescription = NoContentDescription
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(Dimens.Margin.Small))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

object SearchLikedMovieScreen {

    data class Actions(
        val onQueryChange: (String) -> Unit,
        val likeMovie: (TmdbMovieId) -> Unit
    )
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
            actions = SearchLikedMovieScreen.Actions(
                onQueryChange = { searchQuery = it },
                likeMovie = {}
            )
        )
    }
}
