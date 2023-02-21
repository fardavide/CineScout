package cinescout.account.domain.sample

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.Gravatar

object AccountSample {

    val Tmdb = Account.Tmdb(
        gravatar = Gravatar(hash = "hash"),
        username = AccountUsernameSample.Tmdb
    )
    val Trakt = Account.Trakt(
        gravatar = Gravatar(hash = "hash"),
        username = AccountUsernameSample.Trakt
    )
}
