package cinescout.account.tmdb.domain.testdata

import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername

object TmdbAccountTestData {

    val Username = TmdbAccountUsername("Tmdb username")
    val Account = TmdbAccount(
        gravatar = Gravatar(hash = "hash"),
        username = Username
    )
}
