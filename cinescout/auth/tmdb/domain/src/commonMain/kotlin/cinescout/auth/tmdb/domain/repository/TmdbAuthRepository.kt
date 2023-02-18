package cinescout.auth.tmdb.domain.repository

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import kotlinx.coroutines.flow.Flow

interface TmdbAuthRepository {

    fun isLinked(): Flow<Boolean>

    fun link(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>>

    suspend fun notifyTokenAuthorized()

    suspend fun unlink()
}
