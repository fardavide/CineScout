package cinescout.suggestions.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.database.MovieQueries
import cinescout.database.SuggestedMovieQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.suggestions.data.LocalSuggestionDataSource
import cinescout.suggestions.data.local.mapper.DatabaseSuggestedMovieMapper
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.utils.kotlin.nonEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory(binds = [LocalSuggestionDataSource::class])
class RealLocalSuggestionDataSource(
    transacter: Transacter,
    private val databaseSuggestedMovieMapper: DatabaseSuggestedMovieMapper,
    private val movieQueries: MovieQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    private val suggestedMovieQueries: SuggestedMovieQueries,
    private val tvShowQueries: TvShowQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalSuggestionDataSource, Transacter by transacter {

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<SuggestedMovie>>> =
        movieQueries.findAllSuggested()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                databaseSuggestedMovieMapper.toDomainModels(list)
                    .nonEmpty { DataError.Local.NoCache }
            }

    override fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<SuggestedTvShow>>> {
        tvShowQueries
        TODO("Not yet implemented")
    }

    override suspend fun insertSuggestedMovies(suggestedMovies: Collection<SuggestedMovie>) {
        suspendTransaction(writeDispatcher) {
            for (suggestedMovie in suggestedMovies) {
                val (databaseMovie, databaseSuggestion) = databaseSuggestedMovieMapper.toDatabaseModel(suggestedMovie)
                movieQueries.insertMovieObject(databaseMovie)
                suggestedMovieQueries.insertSuggestion(databaseSuggestion)
            }
        }
    }

    override suspend fun insertSuggestedTvShows(suggestedTvShows: Collection<SuggestedTvShow>) {
        TODO("Not yet implemented")
    }
}
