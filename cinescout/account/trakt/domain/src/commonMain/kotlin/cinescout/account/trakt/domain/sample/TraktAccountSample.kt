package cinescout.account.trakt.domain.sample

import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar

object TraktAccountSample {

    val Username = AccountUsername("Trakt username")
    val Account = cinescout.account.domain.model.Account.Trakt(
        gravatar = Gravatar(hash = "hash"),
        username = Username
    )
}
