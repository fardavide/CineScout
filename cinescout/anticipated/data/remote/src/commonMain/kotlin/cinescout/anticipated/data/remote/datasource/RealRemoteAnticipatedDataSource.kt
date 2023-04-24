package cinescout.anticipated.data.remote.datasource

import arrow.core.Either
import cinescout.anticipated.data.datasource.RemoteAnticipatedDataSource
import cinescout.anticipated.data.remote.mapper.TraktAnticipatedMapper
import cinescout.anticipated.data.remote.service.AnticipatedService
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayType
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteAnticipatedDataSource(
    private val anticipatedMapper: TraktAnticipatedMapper,
    private val anticipatedService: AnticipatedService
) : RemoteAnticipatedDataSource {

    override suspend fun getMostAnticipatedIds(
        type: ScreenplayType
    ): Either<NetworkError, List<ScreenplayIds>> =
        anticipatedService.getMostAnticipatedIds(type).map(anticipatedMapper::toScreenplayIds)
}
