package cinescout.suggestions.data.local.mapper

import cinescout.database.model.DatabaseSuggestionSource
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.suggestions.domain.model.SuggestionSource
import org.koin.core.annotation.Factory

@Factory
class DatabaseSuggestionSourceMapper {

    fun toDomainModel(databaseSuggestionSource: DatabaseSuggestionSource) = when (databaseSuggestionSource) {
        DatabaseSuggestionSource.Anticipated -> SuggestionSource.Anticipated
        is DatabaseSuggestionSource.FromLiked -> SuggestionSource.FromLiked(title = databaseSuggestionSource.title)
        is DatabaseSuggestionSource.FromRated -> SuggestionSource.FromRated(
            title = databaseSuggestionSource.title,
            rating = Rating.of(databaseSuggestionSource.rating).getOrThrow()
        )
        is DatabaseSuggestionSource.FromWatchlist ->
            SuggestionSource.FromWatchlist(title = databaseSuggestionSource.title)
        DatabaseSuggestionSource.PersonalSuggestions -> SuggestionSource.PersonalSuggestions
        DatabaseSuggestionSource.Popular -> SuggestionSource.Popular
        DatabaseSuggestionSource.Suggested -> SuggestionSource.Recommended
        DatabaseSuggestionSource.Trending -> SuggestionSource.Trending
    }

    fun toDatabaseModel(suggestionSource: SuggestionSource) = when (suggestionSource) {
        SuggestionSource.Anticipated -> DatabaseSuggestionSource.Anticipated
        is SuggestionSource.FromLiked -> DatabaseSuggestionSource.FromLiked(title = suggestionSource.title)
        is SuggestionSource.FromRated -> DatabaseSuggestionSource.FromRated(
            title = suggestionSource.title,
            rating = suggestionSource.rating.value.toInt()
        )
        is SuggestionSource.FromWatchlist -> DatabaseSuggestionSource.FromWatchlist(title = suggestionSource.title)
        SuggestionSource.PersonalSuggestions -> DatabaseSuggestionSource.PersonalSuggestions
        SuggestionSource.Popular -> DatabaseSuggestionSource.Popular
        SuggestionSource.Recommended -> DatabaseSuggestionSource.Suggested
        SuggestionSource.Trending -> DatabaseSuggestionSource.Trending
    }
}
