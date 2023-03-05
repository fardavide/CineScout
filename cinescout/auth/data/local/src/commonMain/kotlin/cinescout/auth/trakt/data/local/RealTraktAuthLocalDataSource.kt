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
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class RealTraktAuthLocalDataSource(
    private val authStateQueries: TraktAuthStateQueries,
    @Named(DispatcherQualifier.Io) private val dispatcher: CoroutineDispatcher,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : TraktAuthLocalDataSource {

    override suspend fun deleteTokens() {
        withContext(writeDispatcher) {
            authStateQueries.delete()
        }
    }

    override fun findAuthState(): Flow<TraktAuthState> =
        authStateQueries.find().asFlow().mapToOneOrNull(dispatcher).map { it?.toAuthState() ?: TraktAuthState.Idle }

    override suspend fun findTokens(): TraktAccessAndRefreshTokens? = withContext(dispatcher) {
        authStateQueries.find().executeAsOneOrNull()?.getAccessAndRefreshTokens()
    }

    override suspend fun storeAuthState(state: TraktAuthState) {
        withContext(writeDispatcher) {
            authStateQueries.insertState(
                state = state.toDatabaseTraktAuthStateValue(),
                accessToken = state.findDatabaseAccessToken(),
                authorizationCode = state.findDatabaseAuthorizationCode(),
                refreshToken = state.findDatabaseRefreshToken()
            )
        }
    }
}
