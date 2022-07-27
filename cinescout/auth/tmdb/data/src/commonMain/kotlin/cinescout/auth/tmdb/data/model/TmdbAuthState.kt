package cinescout.auth.tmdb.data.model

sealed interface TmdbAuthState {

    object Idle : TmdbAuthState

    data class RequestTokenCreated(val requestToken: TmdbRequestToken) : TmdbAuthState

    data class RequestTokenAuthorized(val requestToken: TmdbAuthorizedRequestToken) : TmdbAuthState

    data class AccessTokenCreated(val accessTokenAndAccountId: TmdbAccessTokenAndAccountId) : TmdbAuthState

    data class Completed(val credentials: TmdbCredentials) : TmdbAuthState
}
