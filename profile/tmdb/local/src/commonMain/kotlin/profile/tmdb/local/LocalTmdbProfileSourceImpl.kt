package profile.tmdb.local

import database.asFlowOfOneOrResourceError
import database.profile.TmdbProfileQueries
import entities.Either
import entities.ResourceError
import entities.model.GravatarImage
import entities.model.TmdbProfile
import kotlinx.coroutines.flow.Flow
import profile.tmdb.LocalTmdbProfileSource

internal class LocalTmdbProfileSourceImpl(
    private val profiles: TmdbProfileQueries
) : LocalTmdbProfileSource {

    override fun findPersonalProfile(): Flow<Either<ResourceError, TmdbProfile>> =
        profiles.selectPersonalProfile { id, tmdbId, username, name, gravatarFullUrl, gravatarThumbUrl, adult ->
            TmdbProfile(
                id = tmdbId,
                username = username,
                name = name,
                avatar = gravatarThumbUrl?.let { GravatarImage(gravatarThumbUrl, gravatarFullUrl) },
                adult = adult
            )
        }.asFlowOfOneOrResourceError()

    override suspend fun storePersonalProfile(profile: TmdbProfile) {
        profiles.insertOrReplace(
            profile.id,
            username = profile.username,
            name = profile.name,
            gravatarFullUrl = profile.avatar?.fullImageUrl,
            gravatarThumbUrl = profile.avatar?.thumbnailUrl,
            adult = profile.adult
        )
    }
}
