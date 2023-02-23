package cinescout.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IsTraktLinked {

    operator fun invoke(): Flow<Boolean>
}

class FakeIsTraktLinked(private val isLinked: Boolean = false) : IsTraktLinked {

    override operator fun invoke(): Flow<Boolean> = flowOf(isLinked)
}
