package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import kotlin.time.Duration.Companion.minutes

@Factory
class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        refresh: Refresh = Refresh.IfExpired(30.minutes)
    ): PagedStore<MovieWithPersonalRating, Paging> =
        movieRepository.getAllRatedMovies(refresh)
}
