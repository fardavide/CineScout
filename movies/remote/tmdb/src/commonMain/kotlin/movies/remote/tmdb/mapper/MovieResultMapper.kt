package movies.remote.tmdb.mapper

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import movies.remote.tmdb.model.MovieResult
import movies.remote.tmdb.movie.MovieService

class MovieResultMapper(
    private val movieService: MovieService,
    private val movieDetailsMapper: MovieDetailsMapper
) : Mapper<MovieResult, Movie> {

    override suspend fun MovieResult.toBusinessModel(): Either<NetworkError, Movie> = movieService.details(id)
        .flatMap(movieDetailsMapper) { it.toBusinessModel() }
}
