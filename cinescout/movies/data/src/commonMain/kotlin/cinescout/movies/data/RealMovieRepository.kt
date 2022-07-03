package cinescout.movies.data

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource
) : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        with(localMovieDataSource) {
            insert(movie)
            insertWatchlist(movie)
        }
    }

    override suspend fun rate(movie: Movie, rating: Rating) {
        with(localMovieDataSource) {
            insert(movie)
            insertRating(movie, rating)
        }
    }
}
