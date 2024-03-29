package cinescout.suggestions.presentation.sample

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.resources.sample.MessageSample
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.state.ForYouState

internal object ForYouStateSample {

    val Loading = ForYouState.Loading

    val NoSuggestedMovies = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedMovies,
        type = ForYouType.Movies
    )

    val NoSuggestedTvShows = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.NoSuggestedTvShows,
        type = ForYouType.TvShows
    )

    val Inception = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouScreenplayUiModelSample.Inception
        ),
        type = ForYouType.Movies
    )

    val Grimm = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Screenplay(
            screenplay = ForYouScreenplayUiModelSample.Grimm
        ),
        type = ForYouType.TvShows
    )

    val NetworkError = ForYouState(
        suggestedItem = ForYouState.SuggestedItem.Error(MessageSample.NoNetworkError),
        type = ForYouType.Movies
    )
}

internal class ForYouScreenPreviewDataProvider : PreviewParameterProvider<ForYouState> {
    override val values = sequenceOf(
        ForYouStateSample.Loading,
        ForYouStateSample.NoSuggestedMovies,
        ForYouStateSample.NoSuggestedTvShows,
        ForYouStateSample.Inception,
        ForYouStateSample.Grimm,
        ForYouStateSample.NetworkError
    )
}
