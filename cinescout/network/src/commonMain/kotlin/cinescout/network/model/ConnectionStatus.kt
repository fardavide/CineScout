package cinescout.network.model

data class ConnectionStatus(
    val device: Connection,
    val tmdb: Connection,
    val trakt: Connection
) {

    enum class Connection {

        Online,
        Offline
    }

    companion object {

        val AllOnline = ConnectionStatus(
            device = Connection.Online,
            tmdb = Connection.Online,
            trakt = Connection.Online
        )

        val AllOffline = ConnectionStatus(
            device = Connection.Offline,
            tmdb = Connection.Offline,
            trakt = Connection.Offline
        )
    }
}
