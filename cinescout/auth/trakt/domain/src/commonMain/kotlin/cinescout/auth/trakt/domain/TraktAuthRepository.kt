package cinescout.auth.trakt.domain

import arrow.core.Either
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import kotlinx.coroutines.flow.Flow

interface TraktAuthRepository {

    fun link(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>>

    suspend fun notifyAppAuthorized(code: TraktAuthorizationCode)
}
