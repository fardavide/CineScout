package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import store.PagedStore
import store.Paging
import store.Refresh
import kotlin.time.Duration.Companion.days

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(7.days)): PagedStore<MovieWithPersonalRating, Paging> =
        movieRepository.getAllRatedMovies(refresh)
}
