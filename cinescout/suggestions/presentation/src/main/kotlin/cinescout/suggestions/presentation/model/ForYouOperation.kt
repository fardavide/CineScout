package cinescout.suggestions.presentation.model

import arrow.core.NonEmptyList
import cinescout.common.model.SuggestionError
import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ForYouOperation

sealed interface ForYouAction : ForYouOperation {

    data class AddToWatchlist(val itemId: TmdbScreenplayId) : ForYouAction

    data class Dislike(val itemId: TmdbScreenplayId) : ForYouAction

    data class Like(val itemId: TmdbScreenplayId) : ForYouAction

    class SelectForYouType(val forYouType: ForYouType) : ForYouAction
}

internal sealed interface ForYouEvent : ForYouOperation {

    data class SuggestedMoviesError(
        val error: SuggestionError,
        val toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ) : ForYouEvent

    data class SuggestedMoviesReceived(
        val movies: NonEmptyList<ForYouScreenplayUiModel>
    ) : ForYouEvent

    data class SuggestedTvShowsError(
        val error: SuggestionError,
        val toErrorState: (SuggestionError) -> ForYouState.SuggestedItem
    ) : ForYouEvent

    data class SuggestedTvShowsReceived(
        val tvShows: NonEmptyList<ForYouScreenplayUiModel>
    ) : ForYouEvent
}
