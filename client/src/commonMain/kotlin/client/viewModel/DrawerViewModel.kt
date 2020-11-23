package client.viewModel

import client.viewModel.DrawerViewModel.ProfileState.LoggedIn
import client.viewModel.DrawerViewModel.ProfileState.LoggedOut
import domain.auth.IsTmdbLoggedIn
import domain.auth.LinkToTmdb
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.ResourceError
import entities.auth.Auth.LoginState
import entities.auth.Auth.LoginState.ApproveRequestToken
import entities.model.Profile
import entities.toRight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrawerViewModel(
    override val scope: CoroutineScope,
    getPersonalTmdbProfile: GetPersonalTmdbProfile,
    isTmdbLoggedIn: IsTmdbLoggedIn,
    private val linkToTmdb: LinkToTmdb
) : CineViewModel {

    private val _tmdbLinkResult: MutableSharedFlow<Either<LinkToTmdb.Error, LinkToTmdb.State>> =
        MutableSharedFlow()

    val tmdbLinkResult: SharedFlow<Either<LinkToTmdb.Error, LinkToTmdb.State>> =
        _tmdbLinkResult.asSharedFlow()

    val profile: StateFlow<ProfileState> =
        isTmdbLoggedIn().flatMapLatest { isLoggedIn ->

            val observeProfile = { getPersonalTmdbProfile() }

            if (isLoggedIn) {
                observeProfile().toProfileState()

            } else {
                tmdbLinkResult.flatMapMerge { either ->
                    if (either.isLoginCompleted())
                        observeProfile().toProfileState(either.isLoggingIn())
                    else
                        emptyFlow()
                }
            }
        }.stateIn(
            scope,
            SharingStarted.Eagerly,
            initialValue = LoggedOut
        )

    fun startLinkingToTmdb() {
        scope.launch {
            linkToTmdb()
                .collect { _tmdbLinkResult.emit(it) }
        }
    }


    private fun Flow<Either<ResourceError, Profile>>.toProfileState(
        isLoggingIn: Boolean = false
    ): Flow<ProfileState> =
        toRight(
            { if (isLoggingIn) ProfileState.LoggingIn else LoggedOut },
            ::LoggedIn
        )

    private fun Either<LinkToTmdb.Error, LinkToTmdb.State>.isLoginCompleted(): Boolean =
        loginState is LoginState.Completed

    private fun Either<LinkToTmdb.Error, LinkToTmdb.State>.isLoggingIn(): Boolean =
        loginState is LoginState.Loading || loginState is ApproveRequestToken.WithoutCode

    private val Either<LinkToTmdb.Error, LinkToTmdb.State>.loginState get() =
        (rightOrNull() as? LinkToTmdb.State.Login)?.loginState


    sealed class ProfileState {
        object LoggedOut : ProfileState()
        object LoggingIn : ProfileState()
        data class LoggedIn(val profile: Profile) : ProfileState()
    }
}
