package cinescout.account.presentation.mapper

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.Gravatar
import cinescout.account.presentation.model.AccountUiModel
import org.koin.core.annotation.Factory

@Factory
class AccountUiModelMapper {

    fun toUiModel(account: Account) = AccountUiModel(
        imageUrl = account.gravatar?.getUrl(Gravatar.Size.LARGE),
        source = sourceFor(account),
        username = account.username.value
    )

    private fun sourceFor(account: Account) = when (account) {
        is Account.Tmdb -> AccountUiModel.Source.Tmdb
        is Account.Trakt -> AccountUiModel.Source.Trakt
    }
}
