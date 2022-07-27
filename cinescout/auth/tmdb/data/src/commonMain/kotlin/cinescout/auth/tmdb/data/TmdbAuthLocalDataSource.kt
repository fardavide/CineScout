package cinescout.auth.tmdb.data

import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbCredentials
import kotlinx.coroutines.flow.Flow

interface TmdbAuthLocalDataSource {

    fun findAuthState(): Flow<TmdbAuthState>

    fun findCredentialsBlocking(): TmdbCredentials?

    suspend fun storeAuthState(state: TmdbAuthState)
}
