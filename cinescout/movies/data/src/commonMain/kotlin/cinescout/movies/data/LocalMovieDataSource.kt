package cinescout.movies.data

import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

interface LocalMovieDataSource {

    suspend fun deleteWatchlist(movieId: TmdbMovieId)

    suspend fun deleteWatchlist(movies: Collection<Movie>)

    fun findAllDislikedMovies(): Flow<List<Movie>>

    fun findAllLikedMovies(): Flow<List<Movie>>

    fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>>

    fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>>

    fun findAllWatchlistMovies(): Flow<List<Movie>>

    fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>>

    fun findMovieWithDetails(id: TmdbMovieId): Flow<MovieWithDetails?>

    fun findMovieCredits(movieId: TmdbMovieId): Flow<MovieCredits?>

    fun findMovieGenres(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieGenres>>

    fun findMovieKeywords(movieId: TmdbMovieId): Flow<MovieKeywords?>

    fun findRecommendationsFor(movieId: TmdbMovieId): Flow<List<Movie>>

    suspend fun insert(movie: Movie)

    suspend fun insert(movie: MovieWithDetails)

    suspend fun insert(movies: Collection<Movie>)

    suspend fun insertCredits(credits: MovieCredits)

    suspend fun insertDisliked(id: TmdbMovieId)

    suspend fun insertGenres(genres: MovieGenres)

    suspend fun insertKeywords(keywords: MovieKeywords)

    suspend fun insertLiked(id: TmdbMovieId)

    suspend fun insertRating(movieId: TmdbMovieId, rating: Rating)

    suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>)

    suspend fun insertRecommendations(movieId: TmdbMovieId, recommendations: List<Movie>)

    suspend fun insertSuggestedMovies(movies: Collection<Movie>)

    suspend fun insertWatchlist(id: TmdbMovieId)

    suspend fun insertWatchlist(movies: Collection<Movie>)
}
