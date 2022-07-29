package cinescout.auth.tmdb.data.local.mapper

import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
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
fun DatabaseTmdbAuthState.toAuthState(): TmdbAuthState =
    when (state) {
        DatabaseTmdbAuthStateValue.Idle -> TmdbAuthState.Idle
        DatabaseTmdbAuthStateValue.RequestTokenCreated -> TmdbAuthState.RequestTokenCreated(
            requestToken = checkNotNull(requestToken).toRequestToken()
        )
        DatabaseTmdbAuthStateValue.RequestTokenAuthorized -> TmdbAuthState.RequestTokenAuthorized(
            requestToken = Authorized(checkNotNull(requestToken).toRequestToken())
        )
        DatabaseTmdbAuthStateValue.AccessTokenCreated -> TmdbAuthState.AccessTokenCreated(
            accessTokenAndAccountId = TmdbAccessTokenAndAccountId(
                accessToken = checkNotNull(accessToken).toAccessToken(),
                accountId = checkNotNull(accountId).toAccountId()
            )
        )
        DatabaseTmdbAuthStateValue.Completed -> TmdbAuthState.Completed(
            credentials = TmdbCredentials(
                accessToken = checkNotNull(accessToken).toAccessToken(),
                accountId = checkNotNull(accountId).toAccountId(),
                sessionId = checkNotNull(sessionId).toSessionId()
            )
        )
    }
fun DatabaseTmdbRequestToken.toRequestToken() = TmdbRequestToken(value)
fun DatabaseTmdbSessionId.toSessionId() = TmdbSessionId(value)
fun TmdbAccessToken.toDatabaseAccessToken() = DatabaseTmdbAccessToken(value)
fun TmdbAccountId.toDatabaseAccountId() = DatabaseTmdbAccountId(value)
fun TmdbAuthState.toDatabaseTmdbAuthState() = DatabaseTmdbAuthState(
    id = 0,
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
fun TmdbRequestToken.toDatabaseRequestToken() = DatabaseTmdbRequestToken(value)
fun TmdbAuthorizedRequestToken.toDatabaseRequestToken() = DatabaseTmdbRequestToken(value)
fun TmdbSessionId.toDatabaseSessionId() = DatabaseTmdbSessionId(value)
