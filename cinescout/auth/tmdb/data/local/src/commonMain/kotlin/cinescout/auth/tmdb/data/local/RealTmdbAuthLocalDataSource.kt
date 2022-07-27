package cinescout.auth.tmdb.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.findDatabaseRequestToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseSessionId
import cinescout.auth.tmdb.data.local.mapper.getCredentials
import cinescout.auth.tmdb.data.local.mapper.toAuthState
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthStateValue
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.database.TmdbAuthStateQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealTmdbAuthLocalDataSource(
    private val authStateQueries: TmdbAuthStateQueries,
    private val dispatcher: CoroutineDispatcher
) : TmdbAuthLocalDataSource {

    override fun findAuthState(): Flow<TmdbAuthState> =
        authStateQueries.find().asFlow().mapToOneOrNull(dispatcher).map { it?.toAuthState() ?: TmdbAuthState.Idle }

    override fun findCredentialsBlocking(): TmdbCredentials? =
        authStateQueries.find().executeAsOneOrNull()?.getCredentials()

    override suspend fun storeAuthState(state: TmdbAuthState) {
        authStateQueries.insertState(
            state = state.toDatabaseTmdbAuthStateValue(),
            accessToken = state.findDatabaseAccessToken(),
            accountId = state.findDatabaseAccountId(),
            requestToken = state.findDatabaseRequestToken(),
            sessionId = state.findDatabaseSessionId()
        )
    }

}
