package cinescout.account.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.account.presentation.model.AccountUiModel

object AccountUiModelSample {

    val Tmdb = AccountUiModel(
        imageUrl = AccountSample.Tmdb.gravatar?.getUrl(Gravatar.Size.MEDIUM),
        source = AccountUiModel.Source.Tmdb,
        username = AccountSample.Tmdb.username.value
    )

    val Trakt = AccountUiModel(
        imageUrl = AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.MEDIUM),
        source = AccountUiModel.Source.Trakt,
        username = AccountSample.Trakt.username.value
    )
}
