package movies

import Actor
import Genre
import Name

data class Movie(
    val name: Name,
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val year: UInt
)
