package cinescout.design.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.design.util.PreviewDataProvider

@Composable
fun ConnectionStatusBanner(uiModel: ConnectionStatusUiModel) {
    when (uiModel) {
        ConnectionStatusUiModel.AllConnected -> {}
        is ConnectionStatusUiModel.DeviceOffline -> Banner(type = Banner.Type.Error, message = uiModel.message)
        is ConnectionStatusUiModel.Error -> Banner(type = Banner.Type.Warning, message = uiModel.message)
    }
}

@Preview
@Composable
private fun ConnectionStatusBannerPreview(
    @PreviewParameter(ConnectionStatusBannerPreviewDataProvider::class) uiModel: ConnectionStatusUiModel
) {
    ConnectionStatusBanner(uiModel)
}

private class ConnectionStatusBannerPreviewDataProvider : PreviewDataProvider<ConnectionStatusUiModel>(
    ConnectionStatusUiModel.AllConnected,
    ConnectionStatusUiModel.DeviceOffline,
    ConnectionStatusUiModel.TmdbOffline,
    ConnectionStatusUiModel.TraktOffline,
    ConnectionStatusUiModel.TmdbAndTraktOffline
)
