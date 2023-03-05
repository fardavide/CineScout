package cinescout.profile.presentation.sample

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.profile.presentation.model.ProfileAccountUiModel

internal object ProfileAccountUiModelSample {

    val Account = ProfileAccountUiModel(
        imageUrl = AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.MEDIUM),
        username = AccountSample.Trakt.username.value
    )
}
