package cinescout.movies.data.local

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
    private val databaseMovieCreditsMapper: DatabaseMovieCreditsMapper,
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val dispatcher: CoroutineDispatcher,
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
    private val suggestedMovieQueries: SuggestedMovieQueries,
    private val watchlistQueries: WatchlistQueries
) : LocalMovieDataSource {

    override fun findAllDislikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllDisliked()
            .asFlow()
            .mapToListOrError(dispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }
            .distinctUntilChanged()

    override fun findAllLikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        movieQueries.findAllLiked()
            .asFlow()
            .mapToListOrError(dispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }
            .distinctUntilChanged()

    override fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>> =
        movieQueries.findAllWithPersonalRating()
            .asFlow()
            .mapToList(dispatcher)
            .map { list ->
                databaseMovieMapper.toMoviesWithRating(list.groupAsMoviesWithRating())
            }

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>> =
        movieQueries.findAllSuggested()
            .asFlow()
            .mapToListOrError(dispatcher)
            .map { either -> either.map { list -> list.map(databaseMovieMapper::toMovie) } }

    override fun findAllWatchlistMovies(): Flow<List<Movie>> =
        movieQueries.findAllInWatchlist()
            .asFlow()
            .mapToList(dispatcher)
            .map { list -> list.map(databaseMovieMapper::toMovie) }

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movieQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrError(dispatcher)
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
            movieQueries.findCastByMovieId(movieId.toDatabaseId()).asFlow().mapToList(dispatcher),
            movieQueries.findCrewByMovieId(movieId.toDatabaseId()).asFlow().mapToList(dispatcher)
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
            .mapToListOrError(dispatcher)
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
            .mapToList(dispatcher)
            .map { list ->
                MovieKeywords(
                    movieId = movieId,
                    keywords = list.map { keyword -> Keyword(id = keyword.genreId.toId(), name = keyword.name) }
                )
            }

    override suspend fun insert(movie: Movie) {
        movieQueries.suspendTransaction(dispatcher) {
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
        movieQueries.suspendTransaction(dispatcher) {
            insertMovie(
                backdropPath = movie.movie.backdropImage.orNull()?.path,
                posterPath = movie.movie.posterImage.orNull()?.path,
                ratingAverage = movie.movie.rating.average.toDatabaseRating(),
                ratingCount = movie.movie.rating.voteCount.toLong(),
                releaseDate = movie.movie.releaseDate.orNull(),
                title = movie.movie.title,
                tmdbId = movie.movie.tmdbId.toDatabaseId()
            )
        }
        genreQueries.suspendTransaction(dispatcher) {
            for (genre in movie.genres) {
                insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
            }
        }
        movieGenreQueries.suspendTransaction(dispatcher) {
            for (genre in movie.genres) {
                insertGenre(
                    movieId = movie.movie.tmdbId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insert(movies: Collection<Movie>) {
        movieQueries.suspendTransaction(dispatcher) {
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
        personQueries.suspendTransaction(dispatcher) {
            for (member in credits.cast + credits.crew) {
                insertPerson(
                    name = member.person.name,
                    profileImagePath = member.person.profileImage.orNull()?.path,
                    tmdbId = member.person.tmdbId.toDatabaseId()
                )
            }
        }

        movieCastMemberQueries.suspendTransaction(dispatcher) {
            for (member in credits.cast) {
                insertCastMember(
                    movieId = credits.movieId.toDatabaseId(),
                    personId = member.person.tmdbId.toDatabaseId(),
                    character = member.character
                )
            }
        }

        movieCrewMemberQueries.suspendTransaction(dispatcher) {
            for (member in credits.crew) {
                insertCrewMember(
                    movieId = credits.movieId.toDatabaseId(),
                    personId = member.person.tmdbId.toDatabaseId(),
                    job = member.job
                )
            }
        }
    }

    override suspend fun insertDisliked(id: TmdbMovieId) {
        likedMovieQueries.suspendTransaction(dispatcher) {
            insert(id.toDatabaseId(), isLiked = false)
        }
    }

    override suspend fun insertGenres(genres: MovieGenres) {
        genreQueries.suspendTransaction(dispatcher) {
            for (genre in genres.genres) {
                insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
            }
        }

        movieGenreQueries.suspendTransaction(dispatcher) {
            for (genre in genres.genres) {
                insertGenre(
                    movieId = genres.movieId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertKeywords(keywords: MovieKeywords) {
        keywordQueries.suspendTransaction(dispatcher) {
            for (keyword in keywords.keywords) {
                insertKeyword(
                    tmdbId = keyword.id.toDatabaseId(),
                    name = keyword.name
                )
            }
        }

        movieKeywordQueries.suspendTransaction(dispatcher) {
            for (keyword in keywords.keywords) {
                insertKeyword(
                    movieId = keywords.movieId.toDatabaseId(),
                    keywordId = keyword.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertLiked(id: TmdbMovieId) {
        likedMovieQueries.suspendTransaction(dispatcher) {
            insert(id.toDatabaseId(), isLiked = true)
        }
    }

    override suspend fun insertRating(movieId: TmdbMovieId, rating: Rating) {
        movieRatingQueries.suspendTransaction(dispatcher) {
            insertRating(tmdbId = movieId.toDatabaseId(), rating = rating.toDatabaseRating())
        }
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>) {
        movieQueries.suspendTransaction(dispatcher) {
            for (movieWithRating in moviesWithRating) {
                insertMovie(
                    backdropPath = movieWithRating.movie.backdropImage.orNull()?.path,
                    posterPath = movieWithRating.movie.posterImage.orNull()?.path,
                    ratingAverage = movieWithRating.movie.rating.average.toDatabaseRating(),
                    ratingCount = movieWithRating.movie.rating.voteCount.toLong(),
                    releaseDate = movieWithRating.movie.releaseDate.orNull(),
                    title = movieWithRating.movie.title,
                    tmdbId = movieWithRating.movie.tmdbId.toDatabaseId()
                )
            }
        }

        movieRatingQueries.suspendTransaction(dispatcher) {
            for (movieWithRating in moviesWithRating) {
                insertRating(
                    tmdbId = movieWithRating.movie.tmdbId.toDatabaseId(),
                    rating = movieWithRating.rating.toDatabaseRating()
                )
            }
        }
    }

    override suspend fun insertSuggestedMovies(movies: Collection<Movie>) {
        suggestedMovieQueries.suspendTransaction(dispatcher) {
            for (movie in movies) {
                insertSuggestion(movie.tmdbId.toDatabaseId(), affinity = 0.0)
            }
        }
    }

    override suspend fun insertWatchlist(id: TmdbMovieId) {
        watchlistQueries.suspendTransaction(dispatcher) {
            insertWatchlist(id.toDatabaseId())
        }
    }

    override suspend fun insertWatchlist(movies: Collection<Movie>) {
        movieQueries.suspendTransaction(dispatcher) {
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

        watchlistQueries.suspendTransaction(dispatcher) {
            for (movie in movies) {
                insertWatchlist(movie.tmdbId.toDatabaseId())
            }
        }
    }
}
