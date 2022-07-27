package cinescout.auth.tmdb.data.local.mapper

import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbAuthState
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.model.TmdbSessionId
import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbAccountId
import cinescout.database.model.DatabaseTmdbAuthState
import cinescout.database.model.DatabaseTmdbAuthStateValue
import cinescout.database.model.DatabaseTmdbCredentials
import cinescout.database.model.DatabaseTmdbRequestToken
import cinescout.database.model.DatabaseTmdbSessionId

fun DatabaseTmdbAccountId.toAccountId() = TmdbAccountId(value)
fun DatabaseTmdbAccessToken.toAccessToken() = TmdbAccessToken(value)
fun DatabaseTmdbAuthState.getCredentials(): TmdbCredentials? =
    if (state == DatabaseTmdbAuthStateValue.Completed) {
        TmdbCredentials(
            accessToken = checkNotNull(accessToken).toAccessToken(),
            accountId = checkNotNull(accountId).toAccountId(),
            sessionId = checkNotNull(sessionId).toSessionId()
        )
    } else {
        null
    }
fun DatabaseTmdbCredentials.toCredentials() = TmdbCredentials(
    accountId = accountId.toAccountId(),
    accessToken = accessToken.toAccessToken(),
    sessionId = sessionId.toSessionId()
)
fun DatabaseTmdbSessionId.toSessionId() = TmdbSessionId(value)
fun TmdbAccessToken.toDatabaseAccessToken() = DatabaseTmdbAccessToken(value)
fun TmdbAccountId.toDatabaseAccountId() = DatabaseTmdbAccountId(value)
fun TmdbAuthState.toDatabaseTmdbAuthState() = DatabaseTmdbAuthState(
    id = 1,
    state = toDatabaseTmdbAuthStateValue(),
    accessToken = findDatabaseAccessToken(),
    accountId = findDatabaseAccountId(),
    requestToken = findDatabaseRequestToken(),
    sessionId = findDatabaseSessionId()
)
fun TmdbAuthState.toDatabaseTmdbAuthStateValue(): DatabaseTmdbAuthStateValue =
    when (this) {
        TmdbAuthState.Idle -> DatabaseTmdbAuthStateValue.Idle
        is TmdbAuthState.RequestTokenCreated -> DatabaseTmdbAuthStateValue.RequestTokenCreated
        is TmdbAuthState.RequestTokenAuthorized -> DatabaseTmdbAuthStateValue.RequestTokenAuthorized
        is TmdbAuthState.AccessTokenCreated -> DatabaseTmdbAuthStateValue.AccessTokenCreated
        is TmdbAuthState.Completed -> DatabaseTmdbAuthStateValue.Completed
    }
fun TmdbAuthState.findDatabaseAccessToken(): DatabaseTmdbAccessToken? =
    when (this) {
        TmdbAuthState.Idle,
        is TmdbAuthState.RequestTokenCreated,
        is TmdbAuthState.RequestTokenAuthorized -> null
        is TmdbAuthState.AccessTokenCreated -> accessTokenAndAccountId.accessToken.toDatabaseAccessToken()
        is TmdbAuthState.Completed -> credentials.accessToken.toDatabaseAccessToken()
    }
fun TmdbAuthState.findDatabaseAccountId(): DatabaseTmdbAccountId? =
    when (this) {
        TmdbAuthState.Idle,
        is TmdbAuthState.RequestTokenCreated,
        is TmdbAuthState.RequestTokenAuthorized -> null
        is TmdbAuthState.AccessTokenCreated -> accessTokenAndAccountId.accountId.toDatabaseAccountId()
        is TmdbAuthState.Completed -> credentials.accountId.toDatabaseAccountId()
    }
fun TmdbAuthState.findDatabaseRequestToken(): DatabaseTmdbRequestToken? =
    when (this) {
        TmdbAuthState.Idle -> null
        is TmdbAuthState.RequestTokenCreated -> requestToken.toDatabaseRequestToken()
        is TmdbAuthState.RequestTokenAuthorized -> requestToken.toDatabaseRequestToken()
        is TmdbAuthState.AccessTokenCreated,
        is TmdbAuthState.Completed -> null
    }
fun TmdbAuthState.findDatabaseSessionId(): DatabaseTmdbSessionId? =
    when (this) {
        TmdbAuthState.Idle,
        is TmdbAuthState.RequestTokenCreated,
        is TmdbAuthState.RequestTokenAuthorized,
        is TmdbAuthState.AccessTokenCreated -> null
        is TmdbAuthState.Completed -> credentials.sessionId.toDatabaseSessionId()
    }
fun TmdbCredentials.toDatabaseCredentials() = DatabaseTmdbCredentials(
    id = 0,
    accountId = accountId.toDatabaseAccountId(),
    accessToken = accessToken.toDatabaseAccessToken(),
    sessionId = sessionId.toDatabaseSessionId()
)
fun TmdbRequestToken.toDatabaseRequestToken() = DatabaseTmdbRequestToken(value)
fun TmdbAuthorizedRequestToken.toDatabaseRequestToken() = DatabaseTmdbRequestToken(value)
fun TmdbSessionId.toDatabaseSessionId() = DatabaseTmdbSessionId(value)
