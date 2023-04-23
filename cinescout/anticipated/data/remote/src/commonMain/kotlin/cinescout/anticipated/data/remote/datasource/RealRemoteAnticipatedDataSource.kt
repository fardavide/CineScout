package cinescout.anticipated.data.remote.datasource

import arrow.core.Either
import cinescout.anticipated.data.datasource.RemoteAnticipatedDataSource
import cinescout.anticipated.data.remote.service.AnticipatedService
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class RealRemoteAnticipatedDataSource(
    private val anticipatedService: AnticipatedService,
    private val screenplayMapper: TraktScreenplayMapper
) : RemoteAnticipatedDataSource {

    override suspend fun getMostAnticipated(type: ScreenplayType): Either<NetworkError, List<Screenplay>> =
        anticipatedService.getMostAnticipated(type).map { either -> either.map(screenplayMapper::toScreenplay) }
}
