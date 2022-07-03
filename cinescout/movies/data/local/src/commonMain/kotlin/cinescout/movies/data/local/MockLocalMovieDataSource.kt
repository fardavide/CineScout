package cinescout.movies.data.local

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class MockLocalMovieDataSource : LocalMovieDataSource {

    private val movies = mutableSetOf<Movie>()
    private val ratings = mutableMapOf<Movie, Rating>()
    private val watchlist = mutableSetOf<Movie>()

    override suspend fun insert(movie: Movie) {
        movies += movie
    }

    override suspend fun insertRating(movie: Movie, rating: Rating) {
        ratings += movie to rating
    }

    override suspend fun insertWatchlist(movie: Movie) {
        watchlist += movie
    }
}
