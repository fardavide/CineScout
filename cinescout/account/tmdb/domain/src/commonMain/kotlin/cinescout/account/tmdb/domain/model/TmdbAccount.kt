package cinescout.account.tmdb.domain.model

import cinescout.account.domain.model.Gravatar

data class TmdbAccount(
    val gravatar: Gravatar?,
    val username: TmdbAccountUsername
)

@JvmInline
value class TmdbAccountUsername(val value: String)
