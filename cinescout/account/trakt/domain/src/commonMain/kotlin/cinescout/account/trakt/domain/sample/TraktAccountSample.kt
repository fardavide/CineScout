package cinescout.account.trakt.domain.sample

import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.domain.model.TraktAccount

object TraktAccountSample {

    val Username = AccountUsername("Trakt username")
    val Account = TraktAccount(
        gravatar = Gravatar(hash = "hash"),
        username = Username
    )
}
