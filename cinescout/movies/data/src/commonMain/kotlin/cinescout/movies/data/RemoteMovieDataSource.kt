package cinescout.movies.data

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

interface RemoteMovieDataSource {

    suspend fun postRating(movie: Movie, rating: Rating)

    suspend fun postWatchlist(movie: Movie)
}
