package cinescout.seasons.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.data.datasource.RemoteSeasonDataSource
import cinescout.seasons.data.remote.mapper.TraktSeasonMapper
import cinescout.seasons.data.remote.service.SeasonService
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteSeasonDataSource(
    private val seasonMapper: TraktSeasonMapper,
    private val service: SeasonService
) : RemoteSeasonDataSource {

    override suspend fun getSeasonsWithEpisodes(
        tvShowIds: TvShowIds
    ): Either<NetworkError, TvShowSeasonsWithEpisodes> = service.getSeasonsWithEpisodes(tvShowIds)
        .map { seasonMapper.toDomainModel(tvShowIds, it) }
}
