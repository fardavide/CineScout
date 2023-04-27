package cinescout.trending.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.trending.data.datasource.RemoteTrendingDataSource
import cinescout.trending.data.remote.mapper.TraktTrendingMapper
import cinescout.trending.data.remote.service.TrendingService
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteTrendingDataSource(
    private val anticipatedMapper: TraktTrendingMapper,
    private val anticipatedService: TrendingService
) : RemoteTrendingDataSource {

    override suspend fun getTrendingIds(type: ScreenplayType): Either<NetworkError, List<ScreenplayIds>> =
        anticipatedService.getMostTrendingIds(type).map(anticipatedMapper::toScreenplayIds)
}
