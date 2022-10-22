package cinescout.suggestions.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.testdata.MessageTextResTestData
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import cinescout.suggestions.presentation.sample.ForYouTvShowUiModelSample

object ForYouScreenPreviewData {

    val Loading = ForYouState.Loading

    val WithNoSuggestions = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions,
        suggestedTvShow = ForYouState.SuggestedTvShow.NoSuggestions
    )

    val WithInception = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.Data(
            movie = ForYouMovieUiModelSample.Inception
        ),
        suggestedTvShow = ForYouState.SuggestedTvShow.NoSuggestions
    )

    val WithGrimm = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions,
        suggestedTvShow = ForYouState.SuggestedTvShow.Data(
            tvShow = ForYouTvShowUiModelSample.Grimm
        )
    )

    val WithSuggestionsError = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.Error(MessageTextResTestData.NoNetworkError),
        suggestedTvShow = ForYouState.SuggestedTvShow.Error(MessageTextResTestData.NoNetworkError)
    )
}

class ForYouScreenPreviewDataProvider : PreviewParameterProvider<ForYouState> {
    override val values = sequenceOf(
        ForYouScreenPreviewData.Loading,
        ForYouScreenPreviewData.WithNoSuggestions,
        ForYouScreenPreviewData.WithInception,
        ForYouScreenPreviewData.WithGrimm,
        ForYouScreenPreviewData.WithSuggestionsError
    )
}
