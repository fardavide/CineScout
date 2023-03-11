package cinescout.movies.domain.usecase

import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.store.RatedMoviesStore
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetAllRatedMovies {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<MovieWithPersonalRating>>
}

@Factory
class RealGetAllRatedMovies(
    private val ratedMoviesStore: RatedMoviesStore
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<MovieWithPersonalRating>> =
        ratedMoviesStore.stream(StoreReadRequest.cached(Unit, refresh = refresh))
}

class FakeGetAllRatedMovies(
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val store: StoreFlow<List<MovieWithPersonalRating>> =
        ratedMovies?.let { storeFlowOf(it) } ?: storeFlowOf(NetworkError.NotFound)
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<MovieWithPersonalRating>> = store
}
