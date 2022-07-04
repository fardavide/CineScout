package cinescout.movies.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import arrow.core.Either
import cinescout.database.MovieQueries
import cinescout.database.MovieRatingQueries
import cinescout.database.WatchlistQueries
import cinescout.error.DataError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mapper.toDatabaseId
import mapper.toDatabaseRating

internal class RealLocalMovieDataSource(
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val dispatcher: CoroutineDispatcher,
    private val movieQueries: MovieQueries,
    private val movieRatingQueries: MovieRatingQueries,
    private val watchlistQueries: WatchlistQueries
) : LocalMovieDataSource {

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movieQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(databaseMovieMapper::toMovie)

    override suspend fun insert(movie: Movie) {
        movieQueries.insertMovie(tmdbId = movie.tmdbId.toDatabaseId(), title = movie.title)
    }

    override suspend fun insertRating(movie: Movie, rating: Rating) {
        movieRatingQueries.insertRating(movie.tmdbId.toDatabaseId(), rating.toDatabaseRating())
    }

    override suspend fun insertWatchlist(movie: Movie) {
        watchlistQueries.insertWatchlist(movie.tmdbId.toDatabaseId())
    }
}
