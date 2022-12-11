package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetAllLikedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<List<Movie>> =
        movieRepository.getAllLikedMovies()
}
