package cinescout.suggestions.presentation.sample

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.resources.sample.MessageSample
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.state.ForYouState

internal object ForYouStateSample {

    val Loading = ForYouState.Loading

    val WithNoSuggestedMovies = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
        type = ForYouType.Movies
    )

    val WithNoSuggestedTvShows = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
        type = ForYouType.TvShows
    )

    val WithInception = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouScreenplayUiModelSample.Inception
        ),
        type = ForYouType.Movies
    )

    val WithGrimm = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouScreenplayUiModelSample.Grimm
        ),
        type = ForYouType.TvShows
    )

    val WithSuggestionsError = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
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
