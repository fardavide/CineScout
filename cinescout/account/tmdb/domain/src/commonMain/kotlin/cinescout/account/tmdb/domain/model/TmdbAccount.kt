package cinescout.account.tmdb.domain.model

import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar

data class TmdbAccount(
    val gravatar: Gravatar?,
    val username: AccountUsername
)
