package cinescout.movies.data

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class RealMovieRepository : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        // TODO
    }

    override suspend fun rate(movie: Movie, rating: Rating) {
        // TODO
    }
}
