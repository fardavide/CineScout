package cinescout.database.model

typealias DatabaseTraktAuthState = cinescout.database.TraktAuthState

enum class DatabaseTraktAuthStateValue {
    Idle,
    AppAuthorized,
    Completed
}
