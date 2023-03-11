package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.store.MovieDetailsKey
import cinescout.movies.domain.store.MovieDetailsStore
import cinescout.store5.StoreFlow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Factory
class GetMovieDetails(
    private val movieDetailsStore: MovieDetailsStore
) {

    operator fun invoke(movieId: TmdbMovieId, refresh: Boolean = false): StoreFlow<MovieWithDetails> =
        movieDetailsStore.stream(StoreReadRequest.cached(MovieDetailsKey(movieId), refresh = refresh))
}
