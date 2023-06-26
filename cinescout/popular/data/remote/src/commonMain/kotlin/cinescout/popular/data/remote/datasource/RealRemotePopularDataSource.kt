package cinescout.popular.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.popular.data.datasource.RemotePopularDataSource
import cinescout.popular.data.remote.mapper.TraktPopularMapper
import cinescout.popular.data.remote.service.PopularService
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import org.koin.core.annotation.Factory

@Factory
internal class RealRemotePopularDataSource(
    private val anticipatedMapper: TraktPopularMapper,
    private val anticipatedService: PopularService
) : RemotePopularDataSource {

    override suspend fun getPopularIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, List<ScreenplayIds>> =
        anticipatedService.getMostPopularIds(type).map(anticipatedMapper::toScreenplayIds)
}
