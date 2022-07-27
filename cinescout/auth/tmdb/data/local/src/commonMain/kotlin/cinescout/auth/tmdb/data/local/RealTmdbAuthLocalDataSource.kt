package cinescout.auth.tmdb.data.local

import cinescout.auth.tmdb.data.TmdbAuthLocalDataSource
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccessToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseAccountId
import cinescout.auth.tmdb.data.local.mapper.findDatabaseRequestToken
import cinescout.auth.tmdb.data.local.mapper.findDatabaseSessionId
import cinescout.auth.tmdb.data.local.mapper.getCredentials
import cinescout.auth.tmdb.data.local.mapper.toDatabaseTmdbAuthStateValue
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.database.TmdbAuthStateQueries

class RealTmdbAuthLocalDataSource(
    private val authStateQueries: TmdbAuthStateQueries
) : TmdbAuthLocalDataSource {

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
