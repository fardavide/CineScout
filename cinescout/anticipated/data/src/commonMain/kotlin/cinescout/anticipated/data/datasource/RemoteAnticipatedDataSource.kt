package cinescout.anticipated.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface RemoteAnticipatedDataSource {

    fun getMostAnticipated(type: ScreenplayType): Either<NetworkError, List<Screenplay>>
}
