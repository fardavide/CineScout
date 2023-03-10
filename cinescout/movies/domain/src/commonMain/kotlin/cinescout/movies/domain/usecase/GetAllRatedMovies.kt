package cinescout.movies.domain.usecase

import cinescout.error.NetworkError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Factory

interface GetAllRatedMovies {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<MovieWithPersonalRating>>
}

@Factory
class RealGetAllRatedMovies(
    private val movieRepository: MovieRepository
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<MovieWithPersonalRating>> =
        movieRepository.getAllRatedMovies(refresh)
}

class FakeGetAllRatedMovies(
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val store: StoreFlow<List<MovieWithPersonalRating>> =
        ratedMovies?.let { storeFlowOf(it) } ?: storeFlowOf(NetworkError.NotFound)
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<MovieWithPersonalRating>> = store
}
