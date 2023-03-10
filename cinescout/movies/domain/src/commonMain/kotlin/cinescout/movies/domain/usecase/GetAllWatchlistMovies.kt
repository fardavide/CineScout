package cinescout.movies.domain.usecase

import cinescout.error.NetworkError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Factory

interface GetAllWatchlistMovies {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<Movie>>
}

@Factory
class RealGetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Boolean) = movieRepository.getAllWatchlistMovies(refresh)
}

class FakeGetAllWatchlistMovies(
    private val watchlist: List<Movie>? = null,
    private val store: StoreFlow<List<Movie>> =
        watchlist?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Boolean) = store
}
