package cinescout.movies.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import cinescout.database.GenreQueries
import cinescout.database.KeywordQueries
import cinescout.database.LikedMovieQueries
import cinescout.database.MovieCastMemberQueries
import cinescout.database.MovieCrewMemberQueries
import cinescout.database.MovieGenreQueries
import cinescout.database.MovieKeywordQueries
import cinescout.database.MovieQueries
import cinescout.database.MovieRatingQueries
import cinescout.database.PersonQueries
import cinescout.database.SuggestedMovieQueries
import cinescout.database.WatchlistQueries
import cinescout.database.mapper.groupAsMoviesWithRating
import cinescout.database.util.mapToListOrError
import cinescout.database.util.mapToOneOrError
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieCreditsMapper
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.data.local.mapper.toId
import cinescout.movies.domain.model.Genre
import cinescout.movies.domain.model.Keyword
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class RealLocalMovieDataSource(
    transacter: Transacter,
    private val databaseMovieCreditsMapper: DatabaseMovieCreditsMapper,
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val genreQueries: GenreQueries,
    private val keywordQueries: KeywordQueries,
    private val likedMovieQueries: LikedMovieQueries,
    private val movieCastMemberQueries: MovieCastMemberQueries,
    private val movieCrewMemberQueries: MovieCrewMemberQueries,
    private val movieGenreQueries: MovieGenreQueries,
    private val movieKeywordQueries: MovieKeywordQueries,
    private val movieQueries: MovieQueries,
    private val movieRatingQueries: MovieRatingQueries,
    private val personQueries: PersonQueries,
    private val readDispatcher: CoroutineDispatcher,
    private val suggestedMovieQueries: SuggestedMovieQueries,
    private val watchlistQueries: WatchlistQueries,
    private val writeDispatcher: CoroutineDispatcher
    ) : LocalMovieDataSource, Transacter by transacter {

    override suspend fun deleteWatchlist(movies: Collection<Movie>) {
        watchlistQueries.deleteById(movies.map { it.tmdbId.toDatabaseId() })
    }

    override fun findAllDislikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllDisliked()
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }
            .distinctUntilChanged()

    override fun findAllLikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllLiked()
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }
            .distinctUntilChanged()

    override fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>> =
        movieQueries.findAllWithPersonalRating()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                databaseMovieMapper.toMoviesWithRating(list.groupAsMoviesWithRating())
            }

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>> =
        movieQueries.findAllSuggested()
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }

    override fun findAllWatchlistMovies(): Flow<List<Movie>> =
        movieQueries.findAllInWatchlist()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.map(databaseMovieMapper::toMovie) }

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movieQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrError(readDispatcher)
            .map { either -> either.map { movie -> databaseMovieMapper.toMovie(movie) } }

    override fun findMovieWithDetails(id: TmdbMovieId): Flow<MovieWithDetails?> =
        combine(
            findMovie(id),
            findMovieGenres(id)
        ) { movieEither, genresEither ->
            either {
                MovieWithDetails(
                    movie = movieEither.bind(),
                    genres = genresEither.bind().genres
                )
            }.orNull()
        }

    override fun findMovieCredits(movieId: TmdbMovieId): Flow<MovieCredits?> =
        combine(
            movieQueries.findCastByMovieId(movieId.toDatabaseId()).asFlow().mapToList(readDispatcher),
            movieQueries.findCrewByMovieId(movieId.toDatabaseId()).asFlow().mapToList(readDispatcher)
        ) { cast, crew ->
            if (cast.isEmpty() && crew.isEmpty()) {
                null
            } else {
                databaseMovieCreditsMapper.toCredits(movieId, cast, crew)
            }
        }

    override fun findMovieGenres(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieGenres>> =
        movieQueries.findGenresByMovieId(movieId.toDatabaseId())
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either ->
                either.map { list ->
                    MovieGenres(
                        movieId = movieId,
                        genres = list.map { genre -> Genre(id = genre.genreId.toId(), name = genre.name) }
                    )
                }
            }

    override fun findMovieKeywords(movieId: TmdbMovieId): Flow<MovieKeywords?> =
        movieQueries.findKeywordsByMovieId(movieId.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                MovieKeywords(
                    movieId = movieId,
                    keywords = list.map { keyword -> Keyword(id = keyword.genreId.toId(), name = keyword.name) }
                )
            }


    override suspend fun insert(movie: Movie) {
        movieQueries.suspendTransaction(writeDispatcher) {
            insertMovie(
                backdropPath = movie.backdropImage.orNull()?.path,
                posterPath = movie.posterImage.orNull()?.path,
                ratingAverage = movie.rating.average.toDatabaseRating(),
                ratingCount = movie.rating.voteCount.toLong(),
                releaseDate = movie.releaseDate.orNull(),
                title = movie.title,
                tmdbId = movie.tmdbId.toDatabaseId()
            )
        }
    }

    override suspend fun insert(movie: MovieWithDetails) {
        suspendTransaction(writeDispatcher) {
            movieQueries.insertMovie(
                backdropPath = movie.movie.backdropImage.orNull()?.path,
                posterPath = movie.movie.posterImage.orNull()?.path,
                ratingAverage = movie.movie.rating.average.toDatabaseRating(),
                ratingCount = movie.movie.rating.voteCount.toLong(),
                releaseDate = movie.movie.releaseDate.orNull(),
                title = movie.movie.title,
                tmdbId = movie.movie.tmdbId.toDatabaseId()
            )
            for (genre in movie.genres) {
                genreQueries.insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
                movieGenreQueries.insertGenre(
                    movieId = movie.movie.tmdbId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insert(movies: Collection<Movie>) {
        movieQueries.suspendTransaction(writeDispatcher) {
            for (movie in movies) {
                insertMovie(
                    backdropPath = movie.backdropImage.orNull()?.path,
                    posterPath = movie.posterImage.orNull()?.path,
                    ratingAverage = movie.rating.average.toDatabaseRating(),
                    ratingCount = movie.rating.voteCount.toLong(),
                    releaseDate = movie.releaseDate.orNull(),
                    title = movie.title,
                    tmdbId = movie.tmdbId.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertCredits(credits: MovieCredits) {
        suspendTransaction(writeDispatcher) {
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

    override suspend fun insertDisliked(id: TmdbMovieId) {
        likedMovieQueries.suspendTransaction(writeDispatcher) {
            insert(id.toDatabaseId(), isLiked = false)
        }
    }

    override suspend fun insertGenres(genres: MovieGenres) {
        suspendTransaction(writeDispatcher) {
            for (genre in genres.genres) {
                genreQueries.insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
                movieGenreQueries.insertGenre(
                    movieId = genres.movieId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertKeywords(keywords: MovieKeywords) {
        suspendTransaction(writeDispatcher) {
            for (keyword in keywords.keywords) {
                keywordQueries.insertKeyword(
                    tmdbId = keyword.id.toDatabaseId(),
                    name = keyword.name
                )
                movieKeywordQueries.insertKeyword(
                    movieId = keywords.movieId.toDatabaseId(),
                    keywordId = keyword.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertLiked(id: TmdbMovieId) {
        likedMovieQueries.suspendTransaction(writeDispatcher) {
            insert(id.toDatabaseId(), isLiked = true)
        }
    }

    override suspend fun insertRating(movieId: TmdbMovieId, rating: Rating) {
        movieRatingQueries.suspendTransaction(writeDispatcher) {
            insertRating(tmdbId = movieId.toDatabaseId(), rating = rating.toDatabaseRating())
        }
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>) {
        suspendTransaction(writeDispatcher) {
            for (movieWithRating in moviesWithRating) {
                val databaseTmdbMovieId = movieWithRating.movie.tmdbId.toDatabaseId()
                movieQueries.insertMovie(
                    backdropPath = movieWithRating.movie.backdropImage.orNull()?.path,
                    posterPath = movieWithRating.movie.posterImage.orNull()?.path,
                    ratingAverage = movieWithRating.movie.rating.average.toDatabaseRating(),
                    ratingCount = movieWithRating.movie.rating.voteCount.toLong(),
                    releaseDate = movieWithRating.movie.releaseDate.orNull(),
                    title = movieWithRating.movie.title,
                    tmdbId = databaseTmdbMovieId
                )
                movieRatingQueries.insertRating(
                    tmdbId = databaseTmdbMovieId,
                    rating = movieWithRating.rating.toDatabaseRating()
                )
            }
        }
    }

    override suspend fun insertSuggestedMovies(movies: Collection<Movie>) {
        suggestedMovieQueries.suspendTransaction(writeDispatcher) {
            for (movie in movies) {
                insertSuggestion(movie.tmdbId.toDatabaseId(), affinity = 0.0)
            }
        }
    }

    override suspend fun insertWatchlist(id: TmdbMovieId) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            insertWatchlist(id.toDatabaseId())
        }
    }

    override suspend fun insertWatchlist(movies: Collection<Movie>) {
        suspendTransaction(writeDispatcher) {
            for (movie in movies) {
                movieQueries.insertMovie(
                    backdropPath = movie.backdropImage.orNull()?.path,
                    posterPath = movie.posterImage.orNull()?.path,
                    ratingAverage = movie.rating.average.toDatabaseRating(),
                    ratingCount = movie.rating.voteCount.toLong(),
                    releaseDate = movie.releaseDate.orNull(),
                    title = movie.title,
                    tmdbId = movie.tmdbId.toDatabaseId()
                )
            }
            for (movie in movies) {
                watchlistQueries.insertWatchlist(movie.tmdbId.toDatabaseId())
            }
        }
    }
}
