package cinescout.auth.trakt.data

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAuthState
import kotlinx.coroutines.flow.Flow

interface TraktAuthLocalDataSource {

    fun findAuthState(): Flow<TraktAuthState>

    suspend fun findTokens(): TraktAccessAndRefreshTokens?

    suspend fun storeAuthState(state: TraktAuthState)
}
