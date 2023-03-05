package cinescout.auth.trakt.data

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

interface TraktAuthLocalDataSource {

    suspend fun deleteTokens()
    fun findAuthState(): Flow<TraktAuthState>

    suspend fun findTokens(): TraktAccessAndRefreshTokens?

    suspend fun storeAuthState(state: TraktAuthState)
}

class FakeTraktAuthLocalDataSource(
    authState: TraktAuthState = TraktAuthState.Idle,
    tokens: TraktAccessAndRefreshTokens? = null
) : TraktAuthLocalDataSource {

    private val mutableAuthState = MutableStateFlow(authState)
    private val mutableTokens = MutableStateFlow(tokens)

    override suspend fun deleteTokens() {
        mutableTokens.emit(null)
    }

    override fun findAuthState(): Flow<TraktAuthState> = mutableAuthState

    override suspend fun findTokens(): TraktAccessAndRefreshTokens? = mutableTokens.first()

    override suspend fun storeAuthState(state: TraktAuthState) {
        mutableAuthState.emit(state)
        if (state is TraktAuthState.Completed) {
            mutableTokens.emit(state.tokens)
        }
    }
}
