package cinescout.suggestions.presentation.sample

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.testdata.MessageSample
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.util.Stack

internal object ForYouStateSample {

    val Loading = ForYouState.Loading

    val WithNoSuggestedMovies = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
        moviesStack = Stack.empty(),
        tvShowsStack = Stack.empty(),
        type = ForYouType.Movies
    )

    val WithNoSuggestedTvShows = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
        moviesStack = Stack.empty(),
        tvShowsStack = Stack.empty(),
        type = ForYouType.TvShows
    )

    val WithInception = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouMovieUiModelSample.Inception
        ),
        moviesStack = Stack.empty(),
        tvShowsStack = Stack.empty(),
        type = ForYouType.Movies
    )

    val WithGrimm = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouTvShowUiModelSample.Grimm
        ),
        moviesStack = Stack.empty(),
        tvShowsStack = Stack.empty(),
        type = ForYouType.TvShows
    )

    val WithSuggestionsError = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
        moviesStack = Stack.empty(),
        tvShowsStack = Stack.empty(),
        type = ForYouType.Movies
    )
}

internal class ForYouScreenPreviewDataProvider : PreviewParameterProvider<ForYouState> {
    override val values = sequenceOf(
        ForYouStateSample.Loading,
        ForYouStateSample.WithNoSuggestedMovies,
        ForYouStateSample.WithNoSuggestedTvShows,
        ForYouStateSample.WithInception,
        ForYouStateSample.WithGrimm,
        ForYouStateSample.WithSuggestionsError
    )
}
