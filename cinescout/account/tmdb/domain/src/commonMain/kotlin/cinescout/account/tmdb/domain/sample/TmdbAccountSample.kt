package cinescout.account.tmdb.domain.sample

import cinescout.account.domain.model.Gravatar

object TmdbAccountSample {

    val Account = cinescout.account.domain.model.Account.Tmdb(
        gravatar = Gravatar(hash = "hash"),
        username = TmdbAccountUsernameSample.Username
    )
}
