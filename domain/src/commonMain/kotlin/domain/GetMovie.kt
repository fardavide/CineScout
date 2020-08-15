package domain

import entities.TmdbId
import entities.movies.Movie
import entities.movies.MovieRepository

class FindMovie(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(id: TmdbId): Movie? =
        movies.find(id)
}
