package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.store.MovieCreditsKey
import cinescout.movies.domain.store.MovieCreditsStore
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetMovieCredits(
    private val movieCreditsStore: MovieCreditsStore
) {

    operator fun invoke(id: TmdbMovieId, refresh: Boolean = true): StoreFlow<MovieCredits> =
        movieCreditsStore.stream(StoreReadRequest.cached(MovieCreditsKey(id), refresh = refresh))
}
