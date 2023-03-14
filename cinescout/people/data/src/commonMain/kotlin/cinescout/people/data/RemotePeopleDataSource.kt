package cinescout.people.data

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemotePeopleDataSource {

    suspend fun getCredits(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayCredits>
}
