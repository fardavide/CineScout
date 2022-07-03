package cinescout.movies.data

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        with(localMovieDataSource) {
            insert(movie)
            insertWatchlist(movie)
        }
        remoteMovieDataSource.postWatchlist(movie)
    }

    override suspend fun rate(movie: Movie, rating: Rating) {
        with(localMovieDataSource) {
            insert(movie)
            insertRating(movie, rating)
        }
        remoteMovieDataSource.postRating(movie, rating)
    }
}
