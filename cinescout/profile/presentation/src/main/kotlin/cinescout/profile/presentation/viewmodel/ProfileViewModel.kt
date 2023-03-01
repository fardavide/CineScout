package cinescout.profile.presentation.viewmodel

import cinescout.profile.presentation.action.ProfileAction
import cinescout.profile.presentation.state.ProfileState
import cinescout.utils.android.CineScoutViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ProfileViewModel : CineScoutViewModel<ProfileAction, ProfileState>(ProfileState.Loading) {

    override fun submit(action: ProfileAction) {
        when (action) {
            ProfileAction.None -> Unit
        }
    }
}
