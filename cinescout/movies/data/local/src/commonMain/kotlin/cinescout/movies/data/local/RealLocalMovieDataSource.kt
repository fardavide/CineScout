package cinescout.movies.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import arrow.core.Either
import cinescout.database.MovieQueries
import cinescout.database.MovieRatingQueries
import cinescout.database.WatchlistQueries
import cinescout.error.DataError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RealLocalMovieDataSource(
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val dispatcher: CoroutineDispatcher,
    private val movieQueries: MovieQueries,
    private val movieRatingQueries: MovieRatingQueries,
    private val watchlistQueries: WatchlistQueries
) : LocalMovieDataSource {

    override fun findAllRatedMovies(): Flow<Either<DataError.Local, List<MovieWithRating>>> =
        movieQueries.findAllWithRating()
            .asFlow()
            .mapToList(dispatcher)
            .map(databaseMovieMapper::toMoviesWithRating)

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movieQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(databaseMovieMapper::toMovie)

    override suspend fun insert(movie: Movie) {
        movieQueries.insertMovie(
            tmdbId = movie.tmdbId.toDatabaseId(),
            releaseDate = movie.releaseDate,
            title = movie.title
        )
    }

    override suspend fun insertRating(movie: Movie, rating: Rating) {
        val databaseId = movie.tmdbId.toDatabaseId()
        movieQueries.insertMovie(tmdbId = databaseId, releaseDate = movie.releaseDate, title = movie.title)
        movieRatingQueries.insertRating(tmdbId = databaseId, rating = rating.toDatabaseRating())
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithRating>) {
        movieQueries.transaction {
            movieRatingQueries.transaction {
                for (movieWithRating in moviesWithRating) {
                    val databaseId = movieWithRating.movie.tmdbId.toDatabaseId()
                    movieQueries.insertMovie(
                        tmdbId = databaseId,
                        releaseDate = movieWithRating.movie.releaseDate,
                        title = movieWithRating.movie.title
                    )
                    movieRatingQueries.insertRating(
                        tmdbId = databaseId,
                        rating = movieWithRating.rating.toDatabaseRating()
                    )
                }
            }
        }
    }

    override suspend fun insertWatchlist(movie: Movie) {
        val databaseId = movie.tmdbId.toDatabaseId()
        movieQueries.insertMovie(tmdbId = databaseId, releaseDate = movie.releaseDate, title = movie.title)
        watchlistQueries.insertWatchlist(databaseId)
    }
}
