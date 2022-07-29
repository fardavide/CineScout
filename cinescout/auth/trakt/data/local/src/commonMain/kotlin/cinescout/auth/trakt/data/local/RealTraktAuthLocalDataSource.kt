package cinescout.auth.trakt.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.auth.trakt.data.TraktAuthLocalDataSource
import cinescout.auth.trakt.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.trakt.data.local.mapper.findDatabaseAuthorizationCode
import cinescout.auth.trakt.data.local.mapper.findDatabaseRefreshToken
import cinescout.auth.trakt.data.local.mapper.getAccessAndRefreshTokens
import cinescout.auth.trakt.data.local.mapper.toAuthState
import cinescout.auth.trakt.data.local.mapper.toDatabaseTraktAuthStateValue
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.database.TraktAuthStateQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealTraktAuthLocalDataSource(
    private val authStateQueries: TraktAuthStateQueries,
    private val dispatcher: CoroutineDispatcher
) : TraktAuthLocalDataSource {

    override fun findAuthState(): Flow<TraktAuthState> =
        authStateQueries.find().asFlow().mapToOneOrNull(dispatcher).map { it?.toAuthState() ?: TraktAuthState.Idle }

    override fun findTokensBlocking(): TraktAccessAndRefreshTokens? =
        authStateQueries.find().executeAsOneOrNull()?.getAccessAndRefreshTokens()

    override suspend fun storeAuthState(state: TraktAuthState) {
        authStateQueries.insertState(
            state = state.toDatabaseTraktAuthStateValue(),
            accessToken = state.findDatabaseAccessToken(),
            authorizationCode = state.findDatabaseAuthorizationCode(),
            refreshToken = state.findDatabaseRefreshToken()
        )
    }

}
