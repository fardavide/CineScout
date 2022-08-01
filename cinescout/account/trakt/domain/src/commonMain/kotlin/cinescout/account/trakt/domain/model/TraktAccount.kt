package cinescout.account.trakt.domain.model

import cinescout.account.domain.model.Gravatar

data class TraktAccount(
    val gravatar: Gravatar?,
    val username: TraktAccountUsername
)

@JvmInline
value class TraktAccountUsername(val value: String)
