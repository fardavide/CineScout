package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface SuggestionSource {

    object Anticipated : SuggestionSource
    data class FromLiked(val title: String) : SuggestionSource
    data class FromRated(val title: String, val rating: Rating) : SuggestionSource
    data class FromWatchlist(val title: String) : SuggestionSource
    object PersonalSuggestions : SuggestionSource
    object Popular : SuggestionSource
    object Suggested : SuggestionSource
    object Trending : SuggestionSource
}

internal sealed interface SuggestionIdSource {

    val sourceIds: ScreenplayIds

    data class Anticipated(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class Liked(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class Rated(override val sourceIds: ScreenplayIds, val rating: Rating) : SuggestionIdSource
    data class Watchlist(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class PersonalSuggestions(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class Popular(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class Suggested(override val sourceIds: ScreenplayIds) : SuggestionIdSource
    data class Trending(override val sourceIds: ScreenplayIds) : SuggestionIdSource

    fun toSuggestionSource(sourceTitle: String) = when (this) {
        is Liked -> SuggestionSource.FromLiked(sourceTitle)
        is PersonalSuggestions -> SuggestionSource.PersonalSuggestions
        is Popular -> SuggestionSource.Popular
        is Rated -> SuggestionSource.FromRated(sourceTitle, rating)
        is Suggested -> SuggestionSource.Suggested
        is Trending -> SuggestionSource.Trending
        is Anticipated -> SuggestionSource.Anticipated
        is Watchlist -> SuggestionSource.FromWatchlist(sourceTitle)
    }
}
