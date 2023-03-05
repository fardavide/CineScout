package cinescout.movies.domain.usecase

import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import org.koin.core.annotation.Factory
import store.Refresh
import store.Store
import store.builder.listStoreOf
import store.builder.storeOf
import kotlin.time.Duration.Companion.days

interface GetAllWatchlistMovies {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): Store<List<Movie>>
}

@Factory
class RealGetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Refresh): Store<List<Movie>> =
        movieRepository.getAllWatchlistMovies(refresh)
}

class FakeGetAllWatchlistMovies(
    private val watchlist: List<Movie>? = null,
    private val store: Store<List<Movie>> =
        watchlist?.let(::listStoreOf) ?: storeOf(DataError.Local.NoCache)
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Refresh): Store<List<Movie>> = store
}
