package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.store.MovieKeywordsKey
import cinescout.movies.domain.store.MovieKeywordsStore
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetMovieKeywords(
    private val movieKeywordsStore: MovieKeywordsStore
) {

    operator fun invoke(movieId: TmdbMovieId, refresh: Boolean = true): StoreFlow<MovieKeywords> =
        movieKeywordsStore.stream(StoreReadRequest.cached(MovieKeywordsKey(movieId), refresh = refresh))
}
