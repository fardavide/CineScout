package cinescout.network.trakt

interface RefreshTraktAccessToken {

    suspend operator fun invoke()
}
