package cinescout.database.model

typealias DatabaseTmdbAuthState = cinescout.database.TmdbAuthState

enum class DatabaseTmdbAuthStateValue {
    Idle,
    RequestTokenCreated,
    RequestTokenAuthorized,
    AccessTokenCreated,
    Completed
}
