package cinescout.people.data.datasource

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.id.TmdbScreenplayId

interface RemotePeopleDataSource {

    suspend fun getCredits(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayCredits>
}

class FakeRemotePeopleDataSource(
    private val credits: ScreenplayCredits? = null
) : RemotePeopleDataSource {

    override suspend fun getCredits(screenplayId: TmdbScreenplayId): Either<NetworkError, ScreenplayCredits> =
        credits?.right() ?: NetworkError.Unknown.left()
}
