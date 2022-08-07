package cinescout.movies.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.database.LikedMovieQueries
import cinescout.database.MovieCastMemberQueries
import cinescout.database.MovieCrewMemberQueries
import cinescout.database.MovieQueries
import cinescout.database.MovieRatingQueries
import cinescout.database.PersonQueries
import cinescout.database.WatchlistQueries
import cinescout.database.util.mapToListOrError
import cinescout.database.util.mapToOneOrError
import cinescout.error.DataError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieCreditsMapper
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class RealLocalMovieDataSource(
    private val databaseMovieCreditsMapper: DatabaseMovieCreditsMapper,
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val dispatcher: CoroutineDispatcher,
    private val likedMovieQueries: LikedMovieQueries,
    private val movieCastMemberQueries: MovieCastMemberQueries,
    private val movieCrewMemberQueries: MovieCrewMemberQueries,
    private val movieQueries: MovieQueries,
    private val movieRatingQueries: MovieRatingQueries,
    private val personQueries: PersonQueries,
    private val watchlistQueries: WatchlistQueries
) : LocalMovieDataSource {

    override fun findAllDislikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllDisliked()
            .asFlow()
            .mapToListOrError(dispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }

    override fun findAllLikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllLiked()
            .asFlow()
            .mapToListOrError(dispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }

    override fun findAllRatedMovies(): Flow<Either<DataError.Local, List<MovieWithPersonalRating>>> =
        movieQueries.findAllWithPersonalRating()
            .asFlow()
            .mapToList(dispatcher)
            .map(databaseMovieMapper::toMoviesWithRating)

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movieQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrError(dispatcher)
            .map { it.map(databaseMovieMapper::toMovie) }

    override fun findMovieCredits(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieCredits>> =
        combine(
            movieQueries.findCastByMovieId(movieId.toDatabaseId()).asFlow().mapToList(dispatcher),
            movieQueries.findCrewByMovieId(movieId.toDatabaseId()).asFlow().mapToList(dispatcher)
        ) { cast, crew ->
            if (cast.isEmpty() && crew.isEmpty()) {
                DataError.Local.NoCache.left()
            } else {
                databaseMovieCreditsMapper.toCredits(movieId, cast, crew).right()
            }
        }

    override suspend fun insert(movie: Movie) {
        movieQueries.insertMovie(
            backdropPath = movie.backdropImage.orNull()?.path,
            posterPath = movie.posterImage.path,
            ratingAverage = movie.rating.average.toDatabaseRating(),
            ratingCount = movie.rating.voteCount.toLong(),
            releaseDate = movie.releaseDate,
            title = movie.title,
            tmdbId = movie.tmdbId.toDatabaseId()
        )
    }

    override suspend fun insert(movies: Collection<Movie>) {
        movieQueries.transaction {
            for (movie in movies) {
                movieQueries.insertMovie(
                    backdropPath = movie.backdropImage.orNull()?.path,
                    posterPath = movie.posterImage.path,
                    ratingAverage = movie.rating.average.toDatabaseRating(),
                    ratingCount = movie.rating.voteCount.toLong(),
                    releaseDate = movie.releaseDate,
                    title = movie.title,
                    tmdbId = movie.tmdbId.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertCredits(credits: MovieCredits) {
        personQueries.transaction {
            movieCastMemberQueries.transaction {
                movieCrewMemberQueries.transaction {

                    for (member in credits.cast) {
                        personQueries.insertPerson(
                            name = member.person.name,
                            profileImagePath = member.person.profileImage.orNull()?.path,
                            tmdbId = member.person.tmdbId.toDatabaseId()
                        )
                        movieCastMemberQueries.insertCastMember(
                            movieId = credits.movieId.toDatabaseId(),
                            personId = member.person.tmdbId.toDatabaseId(),
                            character = member.character
                        )
                    }

                    for (member in credits.crew) {
                        personQueries.insertPerson(
                            name = member.person.name,
                            profileImagePath = member.person.profileImage.orNull()?.path,
                            tmdbId = member.person.tmdbId.toDatabaseId()
                        )
                        movieCrewMemberQueries.insertCrewMember(
                            movieId = credits.movieId.toDatabaseId(),
                            personId = member.person.tmdbId.toDatabaseId(),
                            job = member.job
                        )
                    }
                }
            }
        }
    }

    override suspend fun insertDisliked(id: TmdbMovieId) {
        likedMovieQueries.insert(id.toDatabaseId(), isLiked = false)
    }

    override suspend fun insertLiked(id: TmdbMovieId) {
        likedMovieQueries.insert(id.toDatabaseId(), isLiked = true)
    }

    override suspend fun insertRating(movieId: TmdbMovieId, rating: Rating) {
        movieRatingQueries.insertRating(tmdbId = movieId.toDatabaseId(), rating = rating.toDatabaseRating())
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>) {
        movieQueries.transaction {
            movieRatingQueries.transaction {
                for (movieWithRating in moviesWithRating) {
                    val databaseId = movieWithRating.movie.tmdbId.toDatabaseId()
                    movieQueries.insertMovie(
                        backdropPath = movieWithRating.movie.backdropImage.orNull()?.path,
                        posterPath = movieWithRating.movie.posterImage.path,
                        ratingAverage = movieWithRating.movie.rating.average.toDatabaseRating(),
                        ratingCount = movieWithRating.movie.rating.voteCount.toLong(),
                        releaseDate = movieWithRating.movie.releaseDate,
                        title = movieWithRating.movie.title,
                        tmdbId = databaseId
                    )
                    movieRatingQueries.insertRating(
                        tmdbId = databaseId,
                        rating = movieWithRating.rating.toDatabaseRating()
                    )
                }
            }
        }
    }

    override suspend fun insertWatchlist(id: TmdbMovieId) {
        watchlistQueries.insertWatchlist(id.toDatabaseId())
    }

}
