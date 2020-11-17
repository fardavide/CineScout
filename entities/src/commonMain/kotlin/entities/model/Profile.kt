package entities.model

import entities.TmdbId

data class Profile(
    val id: TmdbId,
    val username: Name,
    val name: Name,
    val avatar: GravatarImage,
    val adult: Boolean
)
