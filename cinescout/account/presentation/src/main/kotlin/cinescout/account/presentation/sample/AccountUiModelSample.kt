package cinescout.account.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.account.presentation.model.AccountUiModel

object AccountUiModelSample {

    val Trakt = AccountUiModel(
        imageUrl = AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.LARGE),
        username = AccountSample.Trakt.username.value
    )
}
