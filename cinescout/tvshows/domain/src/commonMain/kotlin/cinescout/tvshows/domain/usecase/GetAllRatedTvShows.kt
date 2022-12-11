package cinescout.tvshows.domain.usecase

import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import kotlin.time.Duration.Companion.minutes

@Factory
class GetAllRatedTvShows(
    private val tvShowRepository: TvShowRepository
) {

    operator fun invoke(
        refresh: Refresh = Refresh.IfExpired(7.minutes)
    ): PagedStore<TvShowWithPersonalRating, Paging> =
        tvShowRepository.getAllRatedTvShows(refresh)
}
