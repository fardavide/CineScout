package movies.remote.tmdb.mapper

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import movies.remote.tmdb.model.MoviePageResult
import movies.remote.tmdb.movie.MovieService
import util.mapNotNullAsync

class MoviePageResultMapper(
    private val movieService: MovieService,
    private val movieDetailsMapper: MovieDetailsMapper
) : Mapper<MoviePageResult, List<Movie>> {

//    override suspend fun MoviePageResult.toBusinessModel(): List<Movie> =
//        results.mapNotNullAsync {
//            try {
//                movieService.detailsOrThrow(it.id)
//            } catch (e: ClientRequestException) {
//                if (e.response.status.value != 404) throw e
//                null
//            }
//        }.map(movieDetailsMapper) { it.toBusinessModel() }

    override suspend fun MoviePageResult.toBusinessModel(): Either<NetworkError, List<Movie>> =
        results.mapNotNullAsync {
            movieService.details(it.id).rightOrNull()
        }.map(movieDetailsMapper) { it.toBusinessModel() }

}
