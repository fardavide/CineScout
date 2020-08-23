package domain

import entities.movies.DiscoverParams
import entities.movies.MovieRepository

class DiscoverMovies(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(params: DiscoverParams) =
        movies.discover(params)

}
