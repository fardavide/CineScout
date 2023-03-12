package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.screenplay.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetAllDislikedMovies {

    operator fun invoke(): Flow<List<Movie>>
}

@Factory
class RealGetAllDislikedMovies(
    private val movieRepository: MovieRepository
) : GetAllDislikedMovies {

    override operator fun invoke(): Flow<List<Movie>> = movieRepository.getAllDislikedMovies()
}

class FakeGetAllDislikedMovies(
    private val dislikedMovies: List<Movie>? = null
) : GetAllDislikedMovies {

    override operator fun invoke(): Flow<List<Movie>> = flowOf(dislikedMovies ?: emptyList())
}
