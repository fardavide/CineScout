package cinescout.suggestions.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cinescout.design.testdata.MessageTextResTestData
import cinescout.suggestions.presentation.model.ForYouState

object ForYouScreenPreviewData {

    val Loading = ForYouState.Loading

    val NotLoggedIn = ForYouState(
        loggedIn = ForYouState.LoggedIn.False,
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
    )

    val WithNoSuggestions = ForYouState(
        loggedIn = ForYouState.LoggedIn.True,
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.NoSuggestions
    )

    val WithInception = ForYouState(
        loggedIn = ForYouState.LoggedIn.True,
        shouldShowHint = false,
        suggestedMovie = ForYouState.SuggestedMovie.Data(
            movie = ForYouMovieUiModelPreviewData.Inception
        )
    )

    val WithSuggestionsError = ForYouState(
        loggedIn = ForYouState.LoggedIn.True,
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
