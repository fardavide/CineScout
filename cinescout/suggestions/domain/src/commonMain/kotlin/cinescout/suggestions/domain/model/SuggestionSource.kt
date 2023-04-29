package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface SuggestionSource {

    /**
     * Most anticipated screenplays on Trakt.
     */
    object Anticipated : SuggestionSource

    /**
     * Related to liked screenplays.
     */
    data class FromLiked(val title: String) : SuggestionSource

    /**
     * Related to rated screenplays.
     */
    data class FromRated(val title: String, val rating: Rating) : SuggestionSource

    /**
     * Related to watchlist screenplays.
     */
    data class FromWatchlist(val title: String) : SuggestionSource

    /**
     * Personal suggestions from Trakt.
     */
    object PersonalSuggestions : SuggestionSource

    /**
     * Most popular screenplays on Trakt.
     */
    object Popular : SuggestionSource

    /**
     * Recommended screenplays on Trakt.
     */
    object Recommended : SuggestionSource

    /**
     * Trending screenplays on Trakt.
     */
    object Trending : SuggestionSource
}

enum class SuggestionSourceType {
    Anticipated,
    InAppGenerated,
    PersonalSuggestions,
    Popular,
    Recommended,
    Trending
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
        is Suggested -> SuggestionSource.Recommended
        is Trending -> SuggestionSource.Trending
        is Anticipated -> SuggestionSource.Anticipated
        is Watchlist -> SuggestionSource.FromWatchlist(sourceTitle)
    }
}

internal fun SuggestionSource.type() = when (this) {
    is SuggestionSource.Anticipated -> SuggestionSourceType.Anticipated
    is SuggestionSource.FromLiked,
    is SuggestionSource.FromRated,
    is SuggestionSource.FromWatchlist -> SuggestionSourceType.InAppGenerated
    is SuggestionSource.PersonalSuggestions -> SuggestionSourceType.PersonalSuggestions
    is SuggestionSource.Popular -> SuggestionSourceType.Popular
    is SuggestionSource.Recommended -> SuggestionSourceType.Recommended
    is SuggestionSource.Trending -> SuggestionSourceType.Trending
}
