package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val suggestedMovie = state.suggestedMovie) {
            is ForYouState.SuggestedMovie.Data -> MovieItem(model = suggestedMovie.movie)
            is ForYouState.SuggestedMovie.Error -> CenteredErrorText(text = suggestedMovie.message)
            ForYouState.SuggestedMovie.Loading -> CenteredProgress()
            ForYouState.SuggestedMovie.NoSuggestions ->
                CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
        }
    }
}

@Composable
private fun MovieItem(model: ForYouMovieUiModel) {
    Card(modifier = Modifier.padding(Dimens.Margin.Medium)) {
        Column(
            modifier = Modifier
                .padding(Dimens.Margin.Medium)
                .fillMaxHeight(0.5f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = model.title, style = MaterialTheme.typography.titleLarge)
            Text(text = model.releaseYear, style = MaterialTheme.typography.labelMedium)
            LazyRow {
                items(model.actors) { actor ->
                    AsyncImage(
                        modifier = Modifier.padding(Dimens.Margin.XSmall).size(Dimens.Icon.Large).clip(CircleShape),
                        contentDescription = NoContentDescription,
                        contentScale = ContentScale.Crop,
                        model = actor.profileUrl
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MovieItemPreview() {
    CineScoutTheme {
        MovieItem(model = ForYouMovieUiModelPreviewData.Inception)
    }
}
