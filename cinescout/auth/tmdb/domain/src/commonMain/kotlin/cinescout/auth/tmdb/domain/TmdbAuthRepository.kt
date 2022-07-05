package cinescout.auth.tmdb.domain

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb.Error
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb.State
import kotlinx.coroutines.flow.Flow

interface TmdbAuthRepository {

    fun link(): Flow<Either<Error, State>>
}
