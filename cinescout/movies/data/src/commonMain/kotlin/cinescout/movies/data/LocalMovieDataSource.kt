package cinescout.movies.data

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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

    fun findMovieCredits(movieId: TmdbMovieId): Flow<MovieCredits>

    fun findMovieGenres(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieGenres>>

    fun findMovieImages(movieId: TmdbMovieId): Flow<MovieImages>

    fun findMovieKeywords(movieId: TmdbMovieId): Flow<MovieKeywords>

    fun findMoviesByQuery(query: String): Flow<List<Movie>>

    fun findMovieVideos(movieId: TmdbMovieId): Flow<MovieVideos>

    fun findRecommendationsFor(movieId: TmdbMovieId): Flow<List<Movie>>

    suspend fun insert(movie: Movie)

    suspend fun insert(movie: MovieWithDetails)

    suspend fun insert(movies: Collection<Movie>)

    suspend fun insertCredits(credits: MovieCredits)

    suspend fun insertDisliked(id: TmdbMovieId)

    suspend fun insertGenres(genres: MovieGenres)

    suspend fun insertImages(images: MovieImages)

    suspend fun insertKeywords(keywords: MovieKeywords)

    suspend fun insertLiked(id: TmdbMovieId)

    suspend fun insertRating(movieId: TmdbMovieId, rating: Rating)

    suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>)

    suspend fun insertRecommendations(movieId: TmdbMovieId, recommendations: List<Movie>)

    suspend fun insertSuggestedMovies(movies: Collection<Movie>)

    suspend fun insertVideos(videos: MovieVideos)

    suspend fun insertWatchlist(id: TmdbMovieId)

    suspend fun insertWatchlist(movies: Collection<Movie>)
}

class FakeLocalMovieDataSource(
    private val suggestedMovies: Nel<Movie>? = null
) : LocalMovieDataSource {

    override suspend fun deleteWatchlist(movieId: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWatchlist(movies: Collection<Movie>) {
        TODO("Not yet implemented")
    }

    override fun findAllDislikedMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findAllLikedMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>> {
        TODO("Not yet implemented")
    }

    override fun findAllSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>> =
        flowOf(suggestedMovies?.right() ?: DataError.Local.NoCache.left())

    override fun findAllWatchlistMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> {
        TODO("Not yet implemented")
    }

    override fun findMovieWithDetails(id: TmdbMovieId): Flow<MovieWithDetails?> {
        TODO("Not yet implemented")
    }

    override fun findMovieCredits(movieId: TmdbMovieId): Flow<MovieCredits> {
        TODO("Not yet implemented")
    }

    override fun findMovieGenres(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieGenres>> {
        TODO("Not yet implemented")
    }

    override fun findMovieImages(movieId: TmdbMovieId): Flow<MovieImages> {
        TODO("Not yet implemented")
    }

    override fun findMovieKeywords(movieId: TmdbMovieId): Flow<MovieKeywords> {
        TODO("Not yet implemented")
    }

    override fun findMoviesByQuery(query: String): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findMovieVideos(movieId: TmdbMovieId): Flow<MovieVideos> {
        TODO("Not yet implemented")
    }

    override fun findRecommendationsFor(movieId: TmdbMovieId): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movie: Movie) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movie: MovieWithDetails) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movies: Collection<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCredits(credits: MovieCredits) {
        TODO("Not yet implemented")
    }

    override suspend fun insertDisliked(id: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGenres(genres: MovieGenres) {
        TODO("Not yet implemented")
    }

    override suspend fun insertImages(images: MovieImages) {
        TODO("Not yet implemented")
    }

    override suspend fun insertKeywords(keywords: MovieKeywords) {
        TODO("Not yet implemented")
    }

    override suspend fun insertLiked(id: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRating(movieId: TmdbMovieId, rating: Rating) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecommendations(movieId: TmdbMovieId, recommendations: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSuggestedMovies(movies: Collection<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertVideos(videos: MovieVideos) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWatchlist(id: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWatchlist(movies: Collection<Movie>) {
        TODO("Not yet implemented")
    }
}
