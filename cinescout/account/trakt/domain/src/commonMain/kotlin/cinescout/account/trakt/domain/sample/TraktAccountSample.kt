package cinescout.account.trakt.domain.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.account.trakt.domain.model.TraktAccountUsername

object TraktAccountSample {

    val Username = TraktAccountUsername("Trakt username")
    val Account = TraktAccount(
        gravatar = Gravatar(hash = "hash"),
        username = Username
    )
}
