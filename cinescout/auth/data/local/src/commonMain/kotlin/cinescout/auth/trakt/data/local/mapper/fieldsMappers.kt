package cinescout.auth.trakt.data.local.mapper

import cinescout.auth.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktAuthState
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.database.model.DatabaseTraktAccessToken
import cinescout.database.model.DatabaseTraktAuthState
import cinescout.database.model.DatabaseTraktAuthStateValue
import cinescout.database.model.DatabaseTraktAuthorizationCode
import cinescout.database.model.DatabaseTraktRefreshToken
import cinescout.database.model.UniqueDatabaseId

fun DatabaseTraktAccessToken.toAccessToken() = TraktAccessToken(value)
fun DatabaseTraktAuthorizationCode.toAuthorizationCode() = TraktAuthorizationCode(value)
fun DatabaseTraktAuthState.getAccessAndRefreshTokens(): TraktAccessAndRefreshTokens? =
    if (state == DatabaseTraktAuthStateValue.Completed) {
        TraktAccessAndRefreshTokens(
            accessToken = checkNotNull(accessToken).toAccessToken(),
            refreshToken = checkNotNull(refreshToken).toRefreshToken()
        )
    } else {
        null
    }
fun DatabaseTraktAuthState.toAuthState(): TraktAuthState = when (state) {
    DatabaseTraktAuthStateValue.Idle -> TraktAuthState.Idle
    DatabaseTraktAuthStateValue.AppAuthorized -> TraktAuthState.AppAuthorized(
        code = checkNotNull(authorizationCode).toAuthorizationCode()
    )
    DatabaseTraktAuthStateValue.Completed -> TraktAuthState.Completed(
        tokens = TraktAccessAndRefreshTokens(
            accessToken = checkNotNull(accessToken).toAccessToken(),
            refreshToken = checkNotNull(refreshToken).toRefreshToken()
        )
    )
}
fun DatabaseTraktRefreshToken.toRefreshToken() = TraktRefreshToken(value)
fun TraktAccessToken.toDatabaseAccessToken() = DatabaseTraktAccessToken(value)
fun TraktRefreshToken.toDatabaseRefreshToken() = DatabaseTraktRefreshToken(value)
fun TraktAuthorizationCode.toDatabaseAuthorizationCode() = DatabaseTraktAuthorizationCode(value)
fun TraktAuthState.toDatabaseTraktAuthState() = DatabaseTraktAuthState(
    id = UniqueDatabaseId,
    state = toDatabaseTraktAuthStateValue(),
    accessToken = findDatabaseAccessToken(),
    authorizationCode = findDatabaseAuthorizationCode(),
    refreshToken = findDatabaseRefreshToken()
)
fun TraktAuthState.toDatabaseTraktAuthStateValue(): DatabaseTraktAuthStateValue = when (this) {
    TraktAuthState.Idle -> DatabaseTraktAuthStateValue.Idle
    is TraktAuthState.AppAuthorized -> DatabaseTraktAuthStateValue.AppAuthorized
    is TraktAuthState.Completed -> DatabaseTraktAuthStateValue.Completed
}
fun TraktAuthState.findDatabaseAccessToken(): DatabaseTraktAccessToken? = when (this) {
    TraktAuthState.Idle -> null
    is TraktAuthState.AppAuthorized -> null
    is TraktAuthState.Completed -> tokens.accessToken.toDatabaseAccessToken()
}
fun TraktAuthState.findDatabaseAuthorizationCode(): DatabaseTraktAuthorizationCode? = when (this) {
    TraktAuthState.Idle -> null
    is TraktAuthState.AppAuthorized -> code.toDatabaseAuthorizationCode()
    is TraktAuthState.Completed -> null
}
fun TraktAuthState.findDatabaseRefreshToken(): DatabaseTraktRefreshToken? = when (this) {
    TraktAuthState.Idle -> null
    is TraktAuthState.AppAuthorized -> null
    is TraktAuthState.Completed -> tokens.refreshToken.toDatabaseRefreshToken()
}
