package cinescout.network.model

data class NetworkStatus(
    val device: Connection,
    val tmdb: Connection,
    val trakt: Connection
) {

    enum class Connection {

        Online,
        Offline
    }

    companion object {

        val AllOnline = NetworkStatus(
            device = Connection.Online,
            tmdb = Connection.Online,
            trakt = Connection.Online
        )

        val AllOffline = NetworkStatus(
            device = Connection.Offline,
            tmdb = Connection.Offline,
            trakt = Connection.Offline
        )
    }
}
