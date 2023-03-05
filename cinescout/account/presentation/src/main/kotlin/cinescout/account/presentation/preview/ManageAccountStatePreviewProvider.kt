package cinescout.account.presentation.preview

import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.design.util.PreviewDataProvider

internal class ManageAccountStatePreviewProvider : PreviewDataProvider<ManageAccountState>(
    ManageAccountStateSample.Loading,
    ManageAccountStateSample.Error,
    ManageAccountStateSample.NotConnected,
    ManageAccountStateSample.Connected
)
