package cinescout.suggestions.data.local.mapper

import cinescout.database.model.DatabaseSuggestionSource
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.suggestions.domain.model.SuggestionSource
import org.koin.core.annotation.Factory

@Factory
class DatabaseSuggestionSourceMapper {

    fun toDomainModel(databaseSuggestionSource: DatabaseSuggestionSource) = when (databaseSuggestionSource) {
        DatabaseSuggestionSource.FromLiked -> SuggestionSource.FromLiked
        is DatabaseSuggestionSource.FromRated ->
            SuggestionSource.FromRated(Rating.of(databaseSuggestionSource.rating).getOrThrow())
        DatabaseSuggestionSource.FromWatchlist -> SuggestionSource.FromWatchlist
        DatabaseSuggestionSource.PersonalSuggestions -> SuggestionSource.PersonalSuggestions
        DatabaseSuggestionSource.Popular -> SuggestionSource.Popular
        DatabaseSuggestionSource.Suggested -> SuggestionSource.Suggested
        DatabaseSuggestionSource.Trending -> SuggestionSource.Trending
        DatabaseSuggestionSource.Upcoming -> SuggestionSource.Upcoming
    }
    
    fun toDatabaseModel(suggestionSource: SuggestionSource) = when (suggestionSource) {
        SuggestionSource.FromLiked -> DatabaseSuggestionSource.FromLiked
        is SuggestionSource.FromRated ->
            DatabaseSuggestionSource.FromRated(suggestionSource.rating.value.toInt())
        SuggestionSource.FromWatchlist -> DatabaseSuggestionSource.FromWatchlist
        SuggestionSource.PersonalSuggestions -> DatabaseSuggestionSource.PersonalSuggestions
        SuggestionSource.Popular -> DatabaseSuggestionSource.Popular
        SuggestionSource.Suggested -> DatabaseSuggestionSource.Suggested
        SuggestionSource.Trending -> DatabaseSuggestionSource.Trending
        SuggestionSource.Upcoming -> DatabaseSuggestionSource.Upcoming
    }
}
