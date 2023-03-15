package cinescout.people.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.people.data.datasource.RemotePeopleDataSource
import cinescout.people.data.remote.mapper.TmdbScreenplayCreditsMapper
import cinescout.people.data.remote.service.TmdbPeopleService
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class RealRemotePeopleDataSource(
    private val mapper: TmdbScreenplayCreditsMapper,
    private val service: TmdbPeopleService
) : RemotePeopleDataSource {

    override suspend fun getCredits(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayCredits> =
        when (screenplayId) {
            is TmdbScreenplayId.Movie -> service.getMovieCredits(screenplayId)
            is TmdbScreenplayId.TvShow -> service.getTvShowCredits(screenplayId)
        }.map(mapper::toScreenplayCredits)
}
