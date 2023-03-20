package cinescout.design.model

import androidx.annotation.StringRes
import cinescout.resources.R.string
import cinescout.resources.TextRes

sealed interface ConnectionStatusUiModel {

    object AllConnected : ConnectionStatusUiModel

    sealed class Error(val message: TextRes) : ConnectionStatusUiModel {

        constructor(@StringRes messageRes: Int) : this(message = TextRes(messageRes))
    }

    object DeviceOffline : Error(string.connection_status_device_offline)

    object TmdbOffline : Error(string.connection_status_tmdb_offline)

    object TraktOffline : Error(string.connection_status_trakt_offline)

    object TmdbAndTraktOffline : Error(string.connection_status_services_offline)
}
