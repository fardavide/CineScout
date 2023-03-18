package cinescout.search.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface RemoteSearchDataSource {
    
    suspend fun search(
        type: ScreenplayType,
        query: String,
        page: Int
    ): Either<NetworkError, List<Screenplay>>
}
