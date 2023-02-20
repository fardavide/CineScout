package cinescout.auth.trakt.domain

import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

interface TraktAuthRepository {

    fun isLinked(): Flow<Boolean>

    fun link(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>>

    suspend fun notifyAppAuthorized(code: TraktAuthorizationCode)
    suspend fun unlink()
}

class FakeTraktAuthRepository(
    isLinked: Boolean = false
) : TraktAuthRepository {

    var invokedNotifyAppAuthorizedCode: TraktAuthorizationCode? = null
        private set
    private val mutableIsLinked = MutableStateFlow(isLinked)

    override fun isLinked(): Flow<Boolean> = mutableIsLinked

    override fun link(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> {
        mutableIsLinked.value = true
        return flowOf(LinkToTrakt.State.Success.right())
    }

    override suspend fun notifyAppAuthorized(code: TraktAuthorizationCode) {
        invokedNotifyAppAuthorizedCode = code
    }

    override suspend fun unlink() {
        mutableIsLinked.emit(false)
    }
}
