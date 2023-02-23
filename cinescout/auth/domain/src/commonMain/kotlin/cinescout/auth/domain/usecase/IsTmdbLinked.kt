package cinescout.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IsTmdbLinked {

    operator fun invoke(): Flow<Boolean>
}

class FakeIsTmdbLinked(private val isLinked: Boolean) : IsTmdbLinked {

    override fun invoke(): Flow<Boolean> = flowOf(isLinked)
}
