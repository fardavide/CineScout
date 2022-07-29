package cinescout.auth.trakt.data

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAuthState
import kotlinx.coroutines.flow.Flow

interface TraktAuthLocalDataSource {

    fun findAuthState(): Flow<TraktAuthState>

    fun findTokensBlocking(): TraktAccessAndRefreshTokens?

    suspend fun storeAuthState(state: TraktAuthState)
}
