package cinescout.database.model

//data class DatabaseTmdbAuthState(
//    val value: DatabaseTmdbAuthStateValue,
//    val accessToken: DatabaseTmdbAccessToken?,
//    val accountId: DatabaseTmdbAccountId?,
//    val requestToken: DatabaseTmdbRequestToken?,
//    val sessionId: DatabaseTmdbSessionId?
//)

typealias DatabaseTmdbAuthState = cinescout.database.TmdbAuthState

enum class DatabaseTmdbAuthStateValue {
    Idle,
    RequestTokenCreated,
    RequestTokenAuthorized,
    AccessTokenCreated,
    Completed
}
