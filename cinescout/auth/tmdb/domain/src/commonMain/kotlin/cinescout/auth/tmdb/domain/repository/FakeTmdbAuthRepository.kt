package cinescout.auth.tmdb.domain.repository

import arrow.core.Either
import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeTmdbAuthRepository(
    isLinked: Boolean = false
) : TmdbAuthRepository {

    var didNotifyTokenAuthorized: Boolean = false
        private set
    private val mutableIsLinked = MutableStateFlow(isLinked)

    override fun isLinked(): Flow<Boolean> = mutableIsLinked

    override fun link(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>> {
        mutableIsLinked.value = true
        return flowOf(LinkToTmdb.State.Success.right())
    }

    override suspend fun notifyTokenAuthorized() {
        didNotifyTokenAuthorized = true
    }
}
