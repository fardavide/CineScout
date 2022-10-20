package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId

class AddTvShowToWatchlist(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> =
        tvShowRepository.addToWatchlist(tvShowId)
}