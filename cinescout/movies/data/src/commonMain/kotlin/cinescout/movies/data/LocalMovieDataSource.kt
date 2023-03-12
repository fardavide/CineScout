package cinescout.movies.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.MovieImages
import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalMovieDataSource {

    suspend fun deleteWatchlist(movieId: TmdbMovieId)

    suspend fun deleteWatchlistExcept(movieIds: List<TmdbMovieId>)

    fun findAllDislikedMovies(): Flow<List<Movie>>

    fun findAllLikedMovies(): Flow<List<Movie>>

    fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>>

    fun findAllWatchlistMovies(): Flow<List<Movie>>

    fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>>

    fun findMovieWithDetails(id: TmdbMovieId): Flow<MovieWithDetails?>

    fun findMovieCredits(movieId: TmdbMovieId): Flow<MovieCredits>

    fun findMovieGenres(movieId: TmdbMovieId): Flow<Either<DataError.Local, MovieGenres>>

    fun findMovieImages(movieId: TmdbMovieId): Flow<MovieImages>

    fun findMovieKeywords(movieId: TmdbMovieId): Flow<MovieKeywords>

    fun findMoviesByQuery(query: String): Flow<List<Movie>>

    fun findMovieVideos(movieId: TmdbMovieId): Flow<MovieVideos>

    fun findSimilarMovies(movieId: TmdbMovieId): Flow<List<Movie>>

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
    suspend fun insertRatingIds(ids: Collection<MovieIdWithPersonalRating>)

    suspend fun insertSimilarMovies(movieId: TmdbMovieId, similarMovies: List<Movie>)

    suspend fun insertVideos(videos: MovieVideos)
    suspend fun insertWatchlist(id: TmdbMovieId)
    suspend fun insertWatchlist(movies: Collection<Movie>)
    suspend fun insertWatchlistIds(ids: Collection<TmdbMovieId>)
}

class FakeLocalMovieDataSource(
    cachedMovies: List<Movie> = emptyList(),
    cachedMoviesWithDetails: List<MovieWithDetails> = emptyList(),
    cachedRatedMovies: List<MovieWithPersonalRating> = emptyList(),
    cachedWatchlistMovies: List<Movie> = emptyList()
) : LocalMovieDataSource {

    private val mutableCachedMovies = MutableStateFlow(cachedMovies)
    private val mutableCachedMoviesWithDetails = MutableStateFlow(cachedMoviesWithDetails)
    private val mutableCachedRatedMovies = MutableStateFlow(cachedRatedMovies)
    private val mutableCachedWatchlistMovies = MutableStateFlow(cachedWatchlistMovies)

    override suspend fun deleteWatchlist(movieId: TmdbMovieId) {
        mutableCachedWatchlistMovies.emit(mutableCachedWatchlistMovies.value.filterNot { it.tmdbId == movieId })
    }

    override suspend fun deleteWatchlistExcept(movieIds: List<TmdbMovieId>) {
        mutableCachedWatchlistMovies.emit(mutableCachedWatchlistMovies.value.filter { it.tmdbId in movieIds })
    }

    override fun findAllDislikedMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findAllLikedMovies(): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun findAllRatedMovies(): Flow<List<MovieWithPersonalRating>> = mutableCachedRatedMovies

    override fun findAllWatchlistMovies(): Flow<List<Movie>> = mutableCachedWatchlistMovies

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> {
        TODO("Not yet implemented")
    }

    override fun findMovieWithDetails(id: TmdbMovieId): Flow<MovieWithDetails?> =
        mutableCachedMoviesWithDetails.map { movies ->
            movies.find { it.movie.tmdbId == id }
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

    override fun findSimilarMovies(movieId: TmdbMovieId): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movie: Movie) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movie: MovieWithDetails) {
        mutableCachedMoviesWithDetails.emit((mutableCachedMoviesWithDetails.value + movie).distinct())
    }

    override suspend fun insert(movies: Collection<Movie>) {
        mutableCachedMovies.emit((mutableCachedMovies.value + movies).distinct())
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
        val movie = requireCachedMovie(movieId)
        val movieWithPersonalRating = MovieWithPersonalRating(movie, rating)
        mutableCachedRatedMovies.emit((mutableCachedRatedMovies.value + movieWithPersonalRating).distinct())
    }

    override suspend fun insertRatings(moviesWithRating: Collection<MovieWithPersonalRating>) {
        mutableCachedRatedMovies.emit((mutableCachedRatedMovies.value + moviesWithRating).distinct())
    }

    override suspend fun insertRatingIds(ids: Collection<MovieIdWithPersonalRating>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSimilarMovies(movieId: TmdbMovieId, similarMovies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertVideos(videos: MovieVideos) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWatchlist(id: TmdbMovieId) {
        val movie = requireCachedMovie(id)
        mutableCachedWatchlistMovies.emit((mutableCachedWatchlistMovies.value + movie).distinct())
    }

    override suspend fun insertWatchlist(movies: Collection<Movie>) {
        mutableCachedWatchlistMovies.emit((mutableCachedWatchlistMovies.value + movies).distinct())
    }

    override suspend fun insertWatchlistIds(ids: Collection<TmdbMovieId>) {
        TODO("Not yet implemented")
    }

    private fun findCachedMovie(id: TmdbMovieId): Movie? = mutableCachedMovies.value.find { it.tmdbId == id }

    private fun requireCachedMovie(id: TmdbMovieId): Movie =
        checkNotNull(findCachedMovie(id)) { "Movie with id $id not found" }
}
