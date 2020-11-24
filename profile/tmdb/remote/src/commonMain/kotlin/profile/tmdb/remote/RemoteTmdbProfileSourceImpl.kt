package profile.tmdb.remote

import entities.Either
import entities.NetworkError
import entities.TmdbId
import entities.model.GravatarImage
import entities.model.Name
import entities.model.TmdbProfile
import profile.tmdb.RemoteTmdbProfileSource
import profile.tmdb.remote.model.AccountResult

internal class RemoteTmdbProfileSourceImpl(
    private val accountService: AccountService
) : RemoteTmdbProfileSource {

    override suspend fun getPersonalProfile(): Either<NetworkError, TmdbProfile> =
        accountService.getPersonalProfile().map { it.toBusinessModel() }

    // TODO build GravatarImage
    private fun AccountResult.toBusinessModel() = TmdbProfile(
        id = TmdbId(id),
        username = Name(username),
        name = Name(name),
        avatar = GravatarImage("TODO", "TODO"),
        adult = includeAdult
    )
}
