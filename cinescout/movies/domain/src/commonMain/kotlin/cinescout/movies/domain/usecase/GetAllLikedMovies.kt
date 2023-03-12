package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.screenplay.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetAllLikedMovies {

    operator fun invoke(): Flow<List<Movie>>
}

@Factory
class RealGetAllLikedMovies(
    private val movieRepository: MovieRepository
) : GetAllLikedMovies {

    override operator fun invoke(): Flow<List<Movie>> = movieRepository.getAllLikedMovies()
}

class FakeGetAllLikedMovies(
    private val likedMovies: List<Movie>? = null
) : GetAllLikedMovies {

    override operator fun invoke(): Flow<List<Movie>> = flowOf(likedMovies ?: emptyList())
}
