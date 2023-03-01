package cinescout.profile.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.usecase.GetCurrentAccount
import cinescout.profile.presentation.action.ProfileAction
import cinescout.profile.presentation.model.ProfileAccountUiModel
import cinescout.profile.presentation.state.ProfileState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ProfileViewModel(
    private val getCurrentAccount: GetCurrentAccount
) : CineScoutViewModel<ProfileAction, ProfileState>(ProfileState.Loading) {

    init {
        viewModelScope.launch {
            getCurrentAccount().map { accountEither ->
                accountEither.fold(
                    ifLeft = { error ->
                        when (error) {
                            is GetAccountError.Network -> ProfileState.Account.Error
                            GetAccountError.NotConnected -> ProfileState.Account.NotConnected
                        }
                    },
                    ifRight = { account ->
                        val uiModel = ProfileAccountUiModel(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.MEDIUM),
                            username = account.username.value
                        )
                        ProfileState.Account.Connected(uiModel)
                    }
                )
            }.collectLatest { accountState ->
                updateState { currentState -> currentState.copy(account = accountState) }
            }
        }
    }

    override fun submit(action: ProfileAction) {
        when (action) {
            ProfileAction.None -> Unit
        }
    }
}
