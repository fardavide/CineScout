package movies

import Actor
import Name

data class Movie(
    val name: Name,
    val actors: Collection<Actor>,
    val genres: Collection<Name>,
    val year: UInt
)
