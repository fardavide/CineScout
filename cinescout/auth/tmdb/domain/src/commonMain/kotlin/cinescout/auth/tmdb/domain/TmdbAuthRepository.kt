package cinescout.auth.tmdb.domain

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import kotlinx.coroutines.flow.Flow

interface TmdbAuthRepository {

    suspend fun isLinked(): Boolean

    fun link(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>>

    suspend fun notifyTokenAuthorized()
}
