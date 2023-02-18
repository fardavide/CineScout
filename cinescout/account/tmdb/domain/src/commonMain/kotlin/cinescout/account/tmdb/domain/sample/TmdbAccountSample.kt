package cinescout.account.tmdb.domain.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount

object TmdbAccountSample {

    val Account = TmdbAccount(
        gravatar = Gravatar(hash = "hash"),
        username = TmdbAccountUsernameSample.Username
    )
}
