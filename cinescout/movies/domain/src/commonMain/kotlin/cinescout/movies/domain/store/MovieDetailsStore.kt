package cinescout.movies.domain.store

import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface MovieDetailsStore : Store5<MovieDetailsKey, MovieWithDetails>

@JvmInline
value class MovieDetailsKey(val movieId: TmdbMovieId)

class FakeMovieDetailsStore(private val moviesDetails: List<MovieWithDetails>) :
    MovieDetailsStore {

    override fun stream(request: StoreReadRequest<MovieDetailsKey>): StoreFlow<MovieWithDetails> =
        storeFlowOf(moviesDetails.first { it.movie.tmdbId == request.key.movieId })
}
