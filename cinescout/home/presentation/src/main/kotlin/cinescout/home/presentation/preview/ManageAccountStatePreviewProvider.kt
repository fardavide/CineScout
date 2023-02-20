package cinescout.home.presentation.preview

import cinescout.design.util.PreviewDataProvider
import cinescout.home.presentation.model.ManageAccountState
import cinescout.home.presentation.sample.ManageAccountStateSample

internal class ManageAccountStatePreviewProvider : PreviewDataProvider<ManageAccountState>(
    ManageAccountStateSample.Loading,
    ManageAccountStateSample.Error,
    ManageAccountStateSample.NotConnected,
    ManageAccountStateSample.TmdbConnected,
    ManageAccountStateSample.TraktConnected
)
