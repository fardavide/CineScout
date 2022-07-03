package cinescout.movies.data

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

interface LocalMovieDataSource {

    suspend fun insert(movie: Movie)

    suspend fun insertRating(movie: Movie, rating: Rating)

    suspend fun insertWatchlist(movie: Movie)
}
