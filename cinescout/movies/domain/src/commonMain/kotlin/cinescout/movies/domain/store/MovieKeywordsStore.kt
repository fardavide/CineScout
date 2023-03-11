package cinescout.movies.domain.store

import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface MovieKeywordsStore : Store5<MovieKeywordsKey, MovieKeywords>

@JvmInline
value class MovieKeywordsKey(val movieId: TmdbMovieId)

class FakeMovieKeywordsStore(private val moviesKeywords: List<MovieKeywords>) :
    MovieKeywordsStore {

    override fun stream(request: StoreReadRequest<MovieKeywordsKey>): StoreFlow<MovieKeywords> =
        storeFlowOf(moviesKeywords.first { it.movieId == request.key.movieId })
}
