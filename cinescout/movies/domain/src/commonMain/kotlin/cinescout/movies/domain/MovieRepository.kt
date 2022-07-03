package cinescout.movies.domain

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

interface MovieRepository {

    suspend fun addToWatchlist(movie: Movie)

    suspend fun rate(movie: Movie, rating: Rating)
}
