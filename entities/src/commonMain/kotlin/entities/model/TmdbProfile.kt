package entities.model

import entities.TmdbId

interface Profile {
    val username: Name
    val name: Name
    val avatar: GravatarImage?
}

data class TmdbProfile(
    val id: TmdbId,
    override val username: Name,
    override val name: Name,
    override val avatar: GravatarImage?,
    val adult: Boolean
) : Profile

data class TraktProfile(
    override val username: Name,
    override val name: Name,
    override val avatar: GravatarImage?
) : Profile
