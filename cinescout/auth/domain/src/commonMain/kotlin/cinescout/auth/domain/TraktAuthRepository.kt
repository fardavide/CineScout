package cinescout.auth.domain

import arrow.core.Either
import arrow.core.right
import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.domain.usecase.LinkToTrakt
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

    var notifyAppAuthorizedCodeInvoked: TraktAuthorizationCode? = null
        private set

    private val mutableIsLinked = MutableStateFlow(isLinked)

    override fun isLinked(): Flow<Boolean> = mutableIsLinked

    override fun link(): Flow<Either<LinkToTrakt.Error, LinkToTrakt.State>> {
        mutableIsLinked.value = true
        return flowOf(LinkToTrakt.State.Success.right())
    }

    override suspend fun notifyAppAuthorized(code: TraktAuthorizationCode) {
        notifyAppAuthorizedCodeInvoked = code
    }

    override suspend fun unlink() {
        mutableIsLinked.emit(false)
    }
}
