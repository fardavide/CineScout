package cinescout.tvshows.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

interface AddTvShowToWatchlist {

    suspend operator fun invoke(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>
}

@Factory
class RealAddTvShowToWatchlist(
    private val tvShowRepository: TvShowRepository
) : AddTvShowToWatchlist {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> =
        tvShowRepository.addToWatchlist(tvShowId)
}

class FakeAddTvShowToWatchlist : AddTvShowToWatchlist {

    override suspend operator fun invoke(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> =
        Unit.right()
}
