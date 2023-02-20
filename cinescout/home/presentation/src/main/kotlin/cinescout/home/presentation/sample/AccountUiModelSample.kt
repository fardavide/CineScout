package cinescout.home.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.trakt.domain.sample.TraktAccountSample
import cinescout.home.presentation.model.AccountUiModel

object AccountUiModelSample {

    val Tmdb = AccountUiModel(
        imageUrl = TmdbAccountSample.Account.gravatar?.getUrl(Gravatar.Size.MEDIUM),
        source = AccountUiModel.Source.Tmdb,
        username = TmdbAccountSample.Account.username.value
    )

    val Trakt = AccountUiModel(
        imageUrl = TraktAccountSample.Account.gravatar?.getUrl(Gravatar.Size.MEDIUM),
        source = AccountUiModel.Source.Trakt,
        username = TraktAccountSample.Account.username.value
    )
}
