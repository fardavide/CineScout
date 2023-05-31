package cinescout.trending.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.trending.data.datasource.RemoteTrendingDataSource
import cinescout.trending.data.remote.mapper.TraktTrendingMapper
import cinescout.trending.data.remote.service.TrendingService
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteTrendingDataSource(
    private val anticipatedMapper: TraktTrendingMapper,
    private val anticipatedService: TrendingService
) : RemoteTrendingDataSource {

    override suspend fun getTrendingIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, List<ScreenplayIds>> =
        anticipatedService.getMostTrendingIds(type).map(anticipatedMapper::toScreenplayIds)
}
