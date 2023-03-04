package cinescout.suggestions.data.local.mapper

import cinescout.database.model.DatabaseSuggestionSource
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.suggestions.domain.model.SuggestionSource
import org.koin.core.annotation.Factory

@Factory
class DatabaseSuggestionSourceMapper {

    fun toDomainModel(databaseSuggestionSource: DatabaseSuggestionSource) = when (databaseSuggestionSource) {
        is DatabaseSuggestionSource.FromLiked -> SuggestionSource.FromLiked(title = databaseSuggestionSource.title)
        is DatabaseSuggestionSource.FromRated -> SuggestionSource.FromRated(
            title = databaseSuggestionSource.title,
            rating = Rating.of(databaseSuggestionSource.rating).getOrThrow()
        )
        is DatabaseSuggestionSource.FromWatchlist ->
            SuggestionSource.FromWatchlist(title = databaseSuggestionSource.title)
        is DatabaseSuggestionSource.PersonalSuggestions -> SuggestionSource.PersonalSuggestions(
            provider = toDomainProvider(databaseSuggestionSource.provider)
        )
        DatabaseSuggestionSource.Popular -> SuggestionSource.Popular
        DatabaseSuggestionSource.Suggested -> SuggestionSource.Suggested
        DatabaseSuggestionSource.Trending -> SuggestionSource.Trending
        DatabaseSuggestionSource.Upcoming -> SuggestionSource.Upcoming
    }

    fun toDatabaseModel(suggestionSource: SuggestionSource) = when (suggestionSource) {
        is SuggestionSource.FromLiked -> DatabaseSuggestionSource.FromLiked(title = suggestionSource.title)
        is SuggestionSource.FromRated -> DatabaseSuggestionSource.FromRated(
            title = suggestionSource.title,
            rating = suggestionSource.rating.value.toInt()
        )
        is SuggestionSource.FromWatchlist -> DatabaseSuggestionSource.FromWatchlist(title = suggestionSource.title)
        is SuggestionSource.PersonalSuggestions -> DatabaseSuggestionSource.PersonalSuggestions(
            provider = toDatabaseProvider(suggestionSource.provider)
        )
        SuggestionSource.Popular -> DatabaseSuggestionSource.Popular
        SuggestionSource.Suggested -> DatabaseSuggestionSource.Suggested
        SuggestionSource.Trending -> DatabaseSuggestionSource.Trending
        SuggestionSource.Upcoming -> DatabaseSuggestionSource.Upcoming
    }

    private fun toDomainProvider(provider: DatabaseSuggestionSource.PersonalSuggestions.Provider) =
        when (provider) {
            DatabaseSuggestionSource.PersonalSuggestions.Provider.Tmdb ->
                SuggestionSource.PersonalSuggestions.Provider.Tmdb
            DatabaseSuggestionSource.PersonalSuggestions.Provider.Trakt ->
                SuggestionSource.PersonalSuggestions.Provider.Trakt
        }
    
    private fun toDatabaseProvider(provider: SuggestionSource.PersonalSuggestions.Provider) =
        when (provider) {
            SuggestionSource.PersonalSuggestions.Provider.Tmdb ->
                DatabaseSuggestionSource.PersonalSuggestions.Provider.Tmdb
            SuggestionSource.PersonalSuggestions.Provider.Trakt ->
                DatabaseSuggestionSource.PersonalSuggestions.Provider.Trakt
        }
}
