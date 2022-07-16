package cinescout.auth.tmdb.domain

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import kotlinx.coroutines.flow.Flow

interface TmdbAuthRepository {

    fun link(): Flow<Either<LinkToTmdb.Error, LinkToTmdb.State>>
}
