package cinescout.movies.domain.usecase

import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import store.builder.pagedStoreOf
import kotlin.time.Duration.Companion.minutes

interface GetAllRatedMovies {

    operator fun invoke(
        refresh: Refresh = Refresh.IfExpired(7.minutes)
    ): PagedStore<MovieWithPersonalRating, Paging>
}

@Factory
class RealGetAllRatedMovies(
    private val movieRepository: MovieRepository
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging> =
        movieRepository.getAllRatedMovies(refresh)
}

class FakeGetAllRatedMovies(
    private val ratedMovies: List<MovieWithPersonalRating>? = null
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging> =
        ratedMovies?.let(::pagedStoreOf) ?: pagedStoreOf(DataError.Local.NoCache)
}
