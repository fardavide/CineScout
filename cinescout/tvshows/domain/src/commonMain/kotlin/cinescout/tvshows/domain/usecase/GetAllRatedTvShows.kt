package cinescout.tvshows.domain.usecase

import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Factory
import store.Refresh
import store.Store
import store.builder.listStoreOf
import store.builder.storeOf
import kotlin.time.Duration.Companion.minutes

interface GetAllRatedTvShows {

    operator fun invoke(
        refresh: Refresh = Refresh.IfExpired(7.minutes)
    ): Store<List<TvShowWithPersonalRating>>
}

@Factory
class RealGetAllRatedTvShows(
    private val tvShowRepository: TvShowRepository
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Refresh): Store<List<TvShowWithPersonalRating>> =
        tvShowRepository.getAllRatedTvShows(refresh)
}

class FakeGetAllRatedTvShows(
    private val ratedTvShows: List<TvShowWithPersonalRating>? = null
) : GetAllRatedTvShows {

    override operator fun invoke(refresh: Refresh): Store<List<TvShowWithPersonalRating>> =
        ratedTvShows?.let(::listStoreOf) ?: storeOf(DataError.Local.NoCache)
}
