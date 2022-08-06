package cinescout.suggestions.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.CenteredProgress
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun ForYouScreen(modifier: Modifier = Modifier) {
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    ForYouScreen(state = state, modifier = modifier)
}

@Composable
fun ForYouScreen(state: ForYouState, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val suggestedMovie = state.suggestedMovie) {
            is ForYouState.SuggestedMovie.Data -> MovieItem(
                model = suggestedMovie.movie,
                openMovie = { movieId -> openMovieExternally(context, movieId) }
            )
            is ForYouState.SuggestedMovie.Error -> CenteredErrorText(text = suggestedMovie.message)
            ForYouState.SuggestedMovie.Loading -> CenteredProgress()
            ForYouState.SuggestedMovie.NoSuggestions ->
                CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
        }
    }
}

private fun openMovieExternally(context: Context, movieId: TmdbMovieId) {
    context.startActivity(Intent.parseUri("http://www.themoviedb.org/movie/${movieId.value}", 0))
}

@Composable
private fun MovieItem(model: ForYouMovieUiModel, openMovie: (TmdbMovieId) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { openMovie(model.tmdbMovieId) }
            .fillMaxHeight(0.5f)
            .padding(Dimens.Margin.Small)
    ) {
        Box {
            AsyncImage(
                model = model.backdropUrl,
                contentDescription = NoContentDescription,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(Dimens.Margin.Medium)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    AsyncImage(
                        model = model.posterUrl,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.3f)
                            .clip(MaterialTheme.shapes.medium),
                        contentDescription = NoContentDescription
                    )
                    Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
                    Column(modifier = Modifier.padding(vertical = Dimens.Margin.Medium)) {
                        Text(text = model.title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(Dimens.Margin.Small))
                        Text(text = model.releaseYear, style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier.padding(Dimens.Margin.Medium),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(Dimens.Icon.Medium),
                                imageVector = Icons.Default.Star,
                                contentDescription = NoContentDescription
                            )
                            Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
                            Text(text = model.rating, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                LazyRow {
                    items(model.actors) { actor ->
                        AsyncImage(
                            model = actor.profileImageUrl,
                            modifier = Modifier
                                .padding(Dimens.Margin.XSmall)
                                .size(Dimens.Icon.Large)
                                .clip(CircleShape),
                            contentDescription = NoContentDescription,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MovieItemPreview() {
    CineScoutTheme {
        MovieItem(model = ForYouMovieUiModelPreviewData.Inception, openMovie = {})
    }
}
