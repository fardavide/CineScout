package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.CenteredProgress
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
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
        when (val suggestedMovies = state.suggestedMovies) {
            is ForYouState.SuggestedMovies.Data -> LazyColumn {
                items(suggestedMovies.movies, key = { it.tmdbId.value }) {
                    Text(text = it.title)
                }
            }
            is ForYouState.SuggestedMovies.Error -> CenteredErrorText(text = suggestedMovies.message)
            ForYouState.SuggestedMovies.Loading -> CenteredProgress()
            ForYouState.SuggestedMovies.NoSuggestions ->
                CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
        }
    }
}
