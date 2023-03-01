package cinescout.profile.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.profile.presentation.sample.ProfileStateSample
import cinescout.profile.presentation.state.ProfileState

internal class ProfilePreviewProvider : PreviewDataProvider<ProfileState>(
    ProfileStateSample.AccountConnected,
    ProfileStateSample.AccountError,
    ProfileStateSample.AccountNotConnected
)
