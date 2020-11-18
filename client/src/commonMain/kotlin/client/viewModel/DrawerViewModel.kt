package client.viewModel

import client.viewModel.DrawerViewModel.ProfileState.LoggedIn
import client.viewModel.DrawerViewModel.ProfileState.LoggedOut
import domain.auth.LinkToTmdb
import domain.profile.GetPersonalTmdbProfile
import entities.Either
import entities.auth.TmdbAuth
import entities.model.Profile
import entities.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DrawerViewModel(
    override val scope: CoroutineScope,
    getPersonalTmdbProfile: GetPersonalTmdbProfile,
    private val linkToTmdb: LinkToTmdb
) : CineViewModel {

    private val _tmdbLinkResult: MutableStateFlow<Either<LinkToTmdb.Error, LinkToTmdb.State>> =
        MutableStateFlow(LinkToTmdb.State.None.right())

    val tmdbLinkResult: StateFlow<Either<LinkToTmdb.Error, LinkToTmdb.State>> =
        _tmdbLinkResult.asStateFlow()

    val profile: StateFlow<ProfileState> =
        tmdbLinkResult.flatMapMerge { either ->
            if (either == LinkToTmdb.State.Login(TmdbAuth.LoginState.Completed).right())
                getPersonalTmdbProfile()
                    .map { it?.let(::LoggedIn) ?: LoggedOut }
            else emptyFlow()
        }.stateIn(
            scope,
            SharingStarted.Eagerly,
            initialValue = LoggedOut
        )

    fun startLinkingToTmdb() {
        scope.launch {
            linkToTmdb()
                .collect { _tmdbLinkResult.value = it }
        }
    }

    sealed class ProfileState {
        object LoggedOut : ProfileState()
        data class LoggedIn(val profile: Profile) : ProfileState()
    }
}
