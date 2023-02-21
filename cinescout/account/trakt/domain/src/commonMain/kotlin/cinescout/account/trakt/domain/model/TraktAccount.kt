package cinescout.account.trakt.domain.model

import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar

data class TraktAccount(
    val gravatar: Gravatar?,
    val username: AccountUsername
)
