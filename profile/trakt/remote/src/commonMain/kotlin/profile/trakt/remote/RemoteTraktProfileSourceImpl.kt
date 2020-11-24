package profile.trakt.remote

import entities.Either
import entities.NetworkError
import entities.model.GravatarImage
import entities.model.Name
import entities.model.TraktProfile
import profile.trakt.RemoteTraktProfileSource
import profile.trakt.remote.model.SettingsResult

internal class RemoteTraktProfileSourceImpl (
    private val userService: UserService
): RemoteTraktProfileSource {

    override suspend fun getPersonalProfile(): Either<NetworkError, TraktProfile> =
        userService.getPersonalProfile().map { it.toBusinessModel() }

    private fun SettingsResult.toBusinessModel() = TraktProfile(
        name = Name(user.username),
        username = Name(user.name),
        avatar = GravatarImage(fullImageUrl = user.images.avatar.full)
    )
}
