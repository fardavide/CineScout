package cinescout.suggestions.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.testdata.MessageTextResTestData
import cinescout.suggestions.presentation.model.ForYouState

object ForYouScreenPreviewData {

    val Loading = ForYouState.Loading

    val NotLoggedIn = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
    )

    val WithNoSuggestions = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
    )

    val WithInception = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.Data(
            movie = ForYouMovieUiModelPreviewData.Inception
        )
    )

    val WithSuggestionsError = ForYouState(
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.Error(MessageTextResTestData.NoNetworkError)
    )
}

class ForYouScreenPreviewDataProvider : PreviewParameterProvider<ForYouState> {
    override val values = sequenceOf(
        ForYouScreenPreviewData.Loading,
        ForYouScreenPreviewData.NotLoggedIn,
        ForYouScreenPreviewData.WithNoSuggestions,
        ForYouScreenPreviewData.WithInception,
        ForYouScreenPreviewData.WithSuggestionsError
    )
}
