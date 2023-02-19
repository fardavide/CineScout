package cinescout.network.sample

import cinescout.network.model.ConnectionStatus

object ConnectionStatusSample {

    val TmdbOffline = ConnectionStatus(
        device = ConnectionStatus.Connection.Online,
        tmdb = ConnectionStatus.Connection.Offline,
        trakt = ConnectionStatus.Connection.Online
    )

    val TraktOffline = ConnectionStatus(
        device = ConnectionStatus.Connection.Online,
        tmdb = ConnectionStatus.Connection.Online,
        trakt = ConnectionStatus.Connection.Offline
    )
}
