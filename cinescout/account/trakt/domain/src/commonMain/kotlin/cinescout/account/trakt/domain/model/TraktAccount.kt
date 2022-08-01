package cinescout.account.trakt.domain.model

data class TraktAccount(
    val username: TraktAccountUsername
)

@JvmInline
value class TraktAccountUsername(val value: String)
