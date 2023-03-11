package cinescout.movies.domain.store

import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedMoviesStore : Store5<Unit, List<MovieWithPersonalRating>>

class FakeRatedMoviesStore(
    private val ratedMovies: List<MovieWithPersonalRating>? = null
) : RatedMoviesStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<MovieWithPersonalRating>> =
        ratedMovies?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
