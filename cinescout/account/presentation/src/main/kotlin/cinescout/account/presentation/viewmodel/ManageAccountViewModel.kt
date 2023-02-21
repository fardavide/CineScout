package cinescout.account.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.usecase.GetTmdbAccount
import cinescout.account.domain.usecase.GetTraktAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.tmdb.domain.usecase.UnlinkFromTmdb
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.UnlinkFromTrakt
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import store.Refresh
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class ManageAccountViewModel(
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount,
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt,
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val startUpdateSuggestions: StartUpdateSuggestions,
    private val unlinkFromTmdb: UnlinkFromTmdb,
    private val unlinkFromTrakt: UnlinkFromTrakt
) : CineScoutViewModel<ManageAccountAction, ManageAccountState>(ManageAccountState.Loading) {

    init {
        viewModelScope.launch {
            combine(
                getTmdbAccount(Refresh.WithInterval(3.seconds)),
                getTraktAccount(Refresh.WithInterval(3.seconds))
            ) { tmdbAccountEither, traktAccountEither ->
                check(tmdbAccountEither.isLeft() || traktAccountEither.isLeft()) {
                    "Both accounts are connected, this is not supported"
                }
                val tmdbAccountUiModelEither = tmdbAccountEither.map { tmdbAccount ->
                    cinescout.account.presentation.model.AccountUiModel(
                        imageUrl = tmdbAccount.gravatar?.getUrl(Gravatar.Size.MEDIUM),
                        source = cinescout.account.presentation.model.AccountUiModel.Source.Tmdb,
                        username = tmdbAccount.username.value
                    )
                }
                val traktAccountUiModelEither = traktAccountEither.map { traktAccount ->
                    cinescout.account.presentation.model.AccountUiModel(
                        imageUrl = traktAccount.gravatar?.getUrl(Gravatar.Size.MEDIUM),
                        source = cinescout.account.presentation.model.AccountUiModel.Source.Trakt,
                        username = traktAccount.username.value
                    )
                }

                tmdbAccountUiModelEither.fold(
                    ifLeft = { accountError ->
                        if (accountError is GetAccountError.Network) {
                            return@combine ManageAccountState.Account.Error(
                                message = networkErrorMapper.toMessage(accountError.networkError)
                            )
                        }
                    },
                    ifRight = { account ->
                        return@combine ManageAccountState.Account.Connected(account)
                    }
                )

                traktAccountUiModelEither.fold(
                    ifLeft = { accountError ->
                        if (accountError is GetAccountError.Network) {
                            return@combine ManageAccountState.Account.Error(
                                message = networkErrorMapper.toMessage(accountError.networkError)
                            )
                        }
                    },
                    ifRight = { account ->
                        return@combine ManageAccountState.Account.Connected(account)
                    }
                )

                return@combine ManageAccountState.Account.NotConnected

            }.collect { account ->
                updateState { currentState ->
                    currentState.copy(account = account)
                }
            }
        }
    }

    override fun submit(action: ManageAccountAction) {
        when (action) {
            ManageAccountAction.LinkToTmdb -> onLinkToTmdb()
            ManageAccountAction.LinkToTrakt -> onLinkToTrakt()
            ManageAccountAction.NotifyTmdbAppAuthorized -> onNotifyTmdbAppAuthorized()
            is ManageAccountAction.NotifyTraktAppAuthorized -> onNotifyTraktAppAuthorized(action.code)
            ManageAccountAction.UnlinkFromTmdb -> onUnlinkFromTmdb()
            ManageAccountAction.UnlinkFromTrakt -> onUnlinkFromTrakt()
        }
    }

    private fun onLinkToTmdb() {
        viewModelScope.launch {
            linkToTmdb().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) },
                        ifRight = {
                            startUpdateSuggestions(suggestionsMode = SuggestionsMode.Quick)
                            currentState.copy(loginEffect = Effect.of(toLoginState(it)))
                        }
                    )
                }
            }
        }
    }

    private fun onLinkToTrakt() {
        viewModelScope.launch {
            linkToTrakt().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) },
                        ifRight = {
                            startUpdateSuggestions(suggestionsMode = SuggestionsMode.Quick)
                            currentState.copy(loginEffect = Effect.of(toLoginState(it)))
                        }
                    )
                }
            }
        }
    }

    private fun onNotifyTmdbAppAuthorized() {
        viewModelScope.launch {
            notifyTmdbAppAuthorized()
        }
    }

    private fun onNotifyTraktAppAuthorized(code: TraktAuthorizationCode) {
        viewModelScope.launch {
            notifyTraktAppAuthorized(code)
        }
    }

    private fun onUnlinkFromTmdb() {
        viewModelScope.launch {
            unlinkFromTmdb()
        }
    }

    private fun onUnlinkFromTrakt() {
        viewModelScope.launch {
            unlinkFromTrakt()
        }
    }

    private fun toLoginState(state: LinkToTmdb.State): ManageAccountState.Login = when (state) {
        LinkToTmdb.State.Success -> ManageAccountState.Login.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken ->
            ManageAccountState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTmdb.Error): ManageAccountState.Login.Error {
        val message = when (error) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return ManageAccountState.Login.Error(message)
    }

    private fun toLoginState(state: LinkToTrakt.State): ManageAccountState.Login = when (state) {
        LinkToTrakt.State.Success -> ManageAccountState.Login.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp ->
            ManageAccountState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTrakt.Error): ManageAccountState.Login.Error {
        val message = when (error) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return ManageAccountState.Login.Error(message)
    }
}
