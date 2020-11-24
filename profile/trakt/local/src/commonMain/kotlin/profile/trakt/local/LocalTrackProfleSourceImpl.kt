package profile.trakt.local

import database.asFlowOfOneOrResourceError
import database.profile.TraktProfileQueries
import entities.Either
import entities.ResourceError
import entities.model.GravatarImage
import entities.model.TraktProfile
import kotlinx.coroutines.flow.Flow
import profile.trakt.LocalTraktProfileSource

internal class LocalTrackProfleSourceImpl(
    private val profiles: TraktProfileQueries
) : LocalTraktProfileSource {

    override fun findPersonalProfile(): Flow<Either<ResourceError, TraktProfile>> =
        profiles.selectPersonalProfile { id, username, name, gravatarFullUrl ->
            TraktProfile(
                username = username,
                name = name,
                avatar = gravatarFullUrl?.let(::GravatarImage)
            )
        }.asFlowOfOneOrResourceError()

    override suspend fun storePersonalProfile(profile: TraktProfile) {
        profiles.insertOrReplace(
            username = profile.username,
            name = profile.name,
            gravatarFullUrl = profile.avatar?.fullImageUrl
        )
    }
}
