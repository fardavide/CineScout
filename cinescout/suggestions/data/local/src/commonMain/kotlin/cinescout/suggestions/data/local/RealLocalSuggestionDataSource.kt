package cinescout.suggestions.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.database.MovieQueries
import cinescout.database.SuggestionQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.suggestions.data.LocalSuggestionDataSource
import cinescout.suggestions.data.local.mapper.DatabaseSuggestionMapper
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionSourceType
import cinescout.suggestions.domain.model.filterTypes
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory(binds = [LocalSuggestionDataSource::class])
class RealLocalSuggestionDataSource(
    private val databaseSuggestionMapper: DatabaseSuggestionMapper,
    private val movieQueries: MovieQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val suggestionQueries: SuggestionQueries,
    transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalSuggestionDataSource, Transacter by transacter {

    override fun findAllSuggestionIds(
        screenplayTypeFilter: ScreenplayTypeFilter,
        sourceTypes: List<SuggestionSourceType>
    ): Flow<Either<DataError.Local, NonEmptyList<SuggestedScreenplayId>>> = when (screenplayTypeFilter) {
        ScreenplayTypeFilter.All -> suggestionQueries.findAllNotKnown()
        ScreenplayTypeFilter.Movies -> suggestionQueries.findAllNotKnownMovies()
        ScreenplayTypeFilter.TvShows -> suggestionQueries.findAllNotKnownTvShows()
    }
        .asFlow()
        .mapToList(readDispatcher)
        .map { list ->
            when (screenplayTypeFilter) {
                ScreenplayTypeFilter.All -> error("Not supported yet")
                ScreenplayTypeFilter.Movies -> databaseSuggestionMapper.toSuggestedMovieIds(list)
                ScreenplayTypeFilter.TvShows -> databaseSuggestionMapper.toSuggestedTvShowIds(list)
            }.filterTypes(sourceTypes).nonEmpty { DataError.Local.NoCache }
        }

    override suspend fun insertSuggestionIds(suggestions: Collection<SuggestedScreenplayId>) {
        suggestionQueries.suspendTransaction(writeDispatcher) {
            for (suggestion in suggestions) {
                val databaseId = suggestion.screenplayIds.tmdb.toDatabaseId()
                val preexistingSuggestionAffinity = suggestionQueries.find(databaseId)
                    .executeAsOneOrNull()
                    ?.affinity
                    ?.toInt()
                    ?: 0
                if (preexistingSuggestionAffinity < suggestion.affinity.value) {
                    val databaseSuggestion = databaseSuggestionMapper.toDatabaseModel(suggestion)
                    suggestionQueries.insert(databaseSuggestion)
                }
            }
        }
    }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        suspendTransaction(writeDispatcher) {
            for (suggestedMovie in suggestedMovies) {
                val databaseId = suggestedMovie.screenplay.tmdbId.toDatabaseId()
                val preexistingSuggestionAffinity = suggestionQueries.find(databaseId)
                    .executeAsOneOrNull()
                    ?.affinity
                    ?.toInt()
                    ?: 0
                if (preexistingSuggestionAffinity < suggestedMovie.affinity.value) {
                    val (databaseMovie, databaseSuggestion) =
                        databaseSuggestionMapper.toDatabaseModel(suggestedMovie)
                    movieQueries.insertMovieObject(databaseMovie)
                    suggestionQueries.insert(databaseSuggestion)
                }
            }
        }
    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {
        suspendTransaction(writeDispatcher) {
            for (suggestedTvShow in suggestedTvShows) {
                val databaseId = suggestedTvShow.screenplay.tmdbId.toDatabaseId()
                val preexistingSuggestionAffinity = suggestionQueries.find(databaseId)
                    .executeAsOneOrNull()
                    ?.affinity
                    ?.toInt()
                    ?: 0
                if (preexistingSuggestionAffinity < suggestedTvShow.affinity.value) {
                    val (databaseTvShow, databaseSuggestion) =
                        databaseSuggestionMapper.toDatabaseModel(suggestedTvShow)
                    tvShowQueries.insertTvShowObject(databaseTvShow)
                    suggestionQueries.insert(databaseSuggestion)
                }
            }
        }
    }
}
