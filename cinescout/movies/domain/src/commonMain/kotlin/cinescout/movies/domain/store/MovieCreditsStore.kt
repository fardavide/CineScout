package cinescout.movies.domain.store

import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface MovieCreditsStore : Store5<MovieCreditsKey, MovieCredits>

@JvmInline
value class MovieCreditsKey(val movieId: TmdbMovieId)

class FakeMovieCreditsStore(private val moviesCredits: List<MovieCredits>) :
    MovieCreditsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<MovieCreditsKey>): StoreFlow<MovieCredits> =
        storeFlowOf(moviesCredits.first { it.movieId == request.key.movieId })
}
