package cinescout.account.tmdb.domain.testdata

import cinescout.account.tmdb.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername

object TmdbAccountTestData {

    val Account = TmdbAccount(
        gravatar = Gravatar(hash = "hash"),
        username = TmdbAccountUsername("username")
    )
}
