package cinescout.account.presentation.mapper

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.Gravatar
import cinescout.account.presentation.model.AccountUiModel
import org.koin.core.annotation.Factory

@Factory
class AccountUiModelMapper {

    fun toUiModel(account: Account) = AccountUiModel(
        imageUrl = account.gravatar?.getUrl(Gravatar.Size.LARGE),
        username = account.username.value
    )
}
