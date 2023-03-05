package cinescout.account.domain.sample

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.Gravatar

object AccountSample {

    val Trakt = Account(
        gravatar = Gravatar(hash = "hash"),
        username = AccountUsernameSample.Trakt
    )
}
