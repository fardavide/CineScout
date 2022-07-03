package cinescout.movies.data.remote

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

interface TraktRemoteMovieDataSource {

    suspend fun postRating(movie: Movie, rating: Rating)

    suspend fun postWatchlist(movie: Movie)
}
