package cinescout.tvshows.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import org.koin.core.annotation.Factory

@Factory
class RateTvShow(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit> =
        tvShowRepository.rate(tvShowId, rating)
}
