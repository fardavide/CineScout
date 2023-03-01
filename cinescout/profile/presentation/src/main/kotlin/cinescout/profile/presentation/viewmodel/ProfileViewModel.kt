package cinescout.profile.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.usecase.GetCurrentAccount
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.profile.presentation.action.ProfileAction
import cinescout.profile.presentation.state.ProfileState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ProfileViewModel(
    private val getCurrentAccount: GetCurrentAccount,
    private val networkErrorMapper: FakeNetworkErrorToMessageMapper
) : CineScoutViewModel<ProfileAction, ProfileState>(ProfileState.Loading) {

    init {
        viewModelScope.launch {
            getCurrentAccount().map { accountEither ->
                accountEither.fold(
                    ifLeft = { error ->
                        when (error) {
                            is GetAccountError.Network -> toAccountError(error)
                            GetAccountError.NotConnected -> ProfileState.Account.NotConnected
                        }
                    },
                    ifRight = { ProfileState.Account.Connected }
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

    private fun toAccountError(accountError: GetAccountError.Network): ProfileState.Account.Error =
        ProfileState.Account.Error(networkErrorMapper.toMessage(accountError.networkError))
}
