package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

interface TmdbAuthLocalDataSource {

    suspend fun deleteCredentials()

    fun findAuthState(): Flow<TmdbAuthState>

    suspend fun findCredentials(): TmdbCredentials?
    suspend fun storeAuthState(state: TmdbAuthState)
}

class FakeTmdbAuthLocalDataSource(
    authState: TmdbAuthState = TmdbAuthState.Idle,
    credentials: TmdbCredentials? = null
) : TmdbAuthLocalDataSource {

    private val mutableAuthState = MutableStateFlow(authState)
    private val mutableCredentials = MutableStateFlow(credentials)

    override suspend fun deleteCredentials() {
        mutableCredentials.emit(null)
    }

    override fun findAuthState(): Flow<TmdbAuthState> = mutableAuthState

    override suspend fun findCredentials(): TmdbCredentials? = mutableCredentials.first()

    override suspend fun storeAuthState(state: TmdbAuthState) {
        mutableAuthState.emit(state)
    }
}
