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
import cinescout.movies.data.local.mapper.toScreenplayDatabaseId
import cinescout.suggestions.data.LocalSuggestionDataSource
import cinescout.suggestions.data.local.mapper.DatabaseSuggestionMapper
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.tvshows.data.local.mapper.toScreenplayDatabaseId
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory(binds = [LocalSuggestionDataSource::class])
class RealLocalSuggestionDataSource(
    private val databaseSuggestionMapper: DatabaseSuggestionMapper,
    private val movieQueries: MovieQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val suggestionQueries: SuggestionQueries,
    transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalSuggestionDataSource, Transacter by transacter {

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovie>>> =
        movieQueries.findAllSuggested()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                databaseSuggestionMapper.toMovieDomainModels(list)
                    .nonEmpty { DataError.Local.NoCache }
            }

    override fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShow>>> =
        tvShowQueries.findAllSuggested()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                databaseSuggestionMapper.toTvShowDomainModels(list)
                    .nonEmpty { DataError.Local.NoCache }
            }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        suspendTransaction(writeDispatcher) {
            for (suggestedMovie in suggestedMovies) {
                val databaseId = suggestedMovie.movie.tmdbId.toScreenplayDatabaseId()
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
                val databaseId = suggestedTvShow.tvShow.tmdbId.toScreenplayDatabaseId()
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
