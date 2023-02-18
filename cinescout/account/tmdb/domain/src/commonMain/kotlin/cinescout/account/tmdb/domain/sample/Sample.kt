package cinescout.account.tmdb.domain.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername

object Sample {

    val Username = TmdbAccountUsername("Tmdb username")
    val Account = TmdbAccount(
        gravatar = Gravatar(hash = "hash"),
        username = Username
    )
}
