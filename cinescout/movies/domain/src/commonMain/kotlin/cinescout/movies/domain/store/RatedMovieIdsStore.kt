package cinescout.movies.domain.store

import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.ids
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedMovieIdsStore : Store5<Unit, List<MovieIdWithPersonalRating>>

class FakeRatedMovieIdsStore(
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val ratedMovieIds: List<MovieIdWithPersonalRating>? = ratedMovies?.ids()
) : RatedMovieIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<MovieIdWithPersonalRating>> =
        ratedMovieIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
