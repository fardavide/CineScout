package cinescout.recommended.data.remote.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.recommended.data.datasource.RemoteRecommendedDataSource
import cinescout.recommended.data.remote.mapper.TraktRecommendedMapper
import cinescout.recommended.data.remote.service.RecommendedService
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteRecommendedDataSource(
    private val anticipatedMapper: TraktRecommendedMapper,
    private val anticipatedService: RecommendedService
) : RemoteRecommendedDataSource {

    override suspend fun getRecommendedIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, List<ScreenplayIds>> =
        anticipatedService.getMostRecommendedIds(type).map(anticipatedMapper::toScreenplayIds)
}
