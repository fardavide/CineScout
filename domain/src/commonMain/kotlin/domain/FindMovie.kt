package domain

import entities.Either
import entities.ResourceError
import entities.TmdbId
import entities.movies.Movie
import entities.movies.MovieRepository

class FindMovie(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(id: TmdbId): Either<ResourceError, Movie> =
        movies.find(id)
}
