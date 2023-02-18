package cinescout.auth.tmdb.domain.usecase

import cinescout.auth.tmdb.domain.repository.TmdbAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Factory

interface IsTmdbLinked {

    operator fun invoke(): Flow<Boolean>
}

@Factory
internal class RealIsTmdbLinked(
    private val tmdbAuthRepository: TmdbAuthRepository
) : IsTmdbLinked {
    override fun invoke(): Flow<Boolean> = tmdbAuthRepository.isLinked()
}

class FakeIsTmdbLinked(
    isLinked: Boolean = false
) : IsTmdbLinked {

    private val mutableIsLinked = MutableStateFlow(isLinked)

    override fun invoke(): Flow<Boolean> = mutableIsLinked

    suspend fun setLinked() {
        mutableIsLinked.emit(true)
    }
}
