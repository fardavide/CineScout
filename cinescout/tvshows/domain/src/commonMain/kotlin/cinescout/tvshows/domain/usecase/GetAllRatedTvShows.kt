package cinescout.tvshows.domain.usecase

import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import store.builder.pagedStoreOf
import kotlin.time.Duration.Companion.minutes

interface GetAllRatedTvShows {

    operator fun invoke(
        refresh: Refresh = Refresh.IfExpired(7.minutes)
    ): PagedStore<TvShowWithPersonalRating, Paging>
}

@Factory
class RealGetAllRatedTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Refresh): PagedStore<TvShowWithPersonalRating, Paging> =
        tvShowRepository.getAllRatedTvShows(refresh)
}

class FakeGetAllRatedTvShows(
    private val ratedTvShows: List<TvShowWithPersonalRating>? = null
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Refresh): PagedStore<TvShowWithPersonalRating, Paging> =
        ratedTvShows?.let(::pagedStoreOf) ?: pagedStoreOf(DataError.Local.NoCache)
}
