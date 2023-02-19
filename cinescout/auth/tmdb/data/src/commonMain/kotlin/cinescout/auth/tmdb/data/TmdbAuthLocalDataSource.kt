package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TmdbAuthLocalDataSource {

    fun findAuthState(): Flow<TmdbAuthState>

    suspend fun findCredentials(): TmdbCredentials?

    suspend fun storeAuthState(state: TmdbAuthState)
}

class FakeTmdbAuthLocalDataSource(
    authState: TmdbAuthState = TmdbAuthState.Idle,
    private val credentials: TmdbCredentials? = null
) : TmdbAuthLocalDataSource {

    private val mutableAuthState = MutableStateFlow(authState)

    override fun findAuthState(): Flow<TmdbAuthState> = mutableAuthState

    override suspend fun findCredentials(): TmdbCredentials? = credentials

    override suspend fun storeAuthState(state: TmdbAuthState) {
        mutableAuthState.emit(state)
    }
}
