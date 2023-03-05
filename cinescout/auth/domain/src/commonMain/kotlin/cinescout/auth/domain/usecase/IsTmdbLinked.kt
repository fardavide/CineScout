package cinescout.auth.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IsTmdbLinked {

    operator fun invoke(): Flow<Boolean>
}

