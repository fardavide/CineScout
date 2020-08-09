package movies

import Name

data class Movie(
    val name: Name,
    val actors: Collection<Name>,
    val genres: Collection<Name>,
    val year: UInt
)
