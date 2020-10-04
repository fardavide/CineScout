package entities.movies

import entities.Actor
import entities.Genre
import entities.Name
import entities.Poster
import entities.UserRating
import entities.TmdbId

data class Movie(
    val id: TmdbId,
    val name: Name,
    val poster: Poster?,
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val year: UInt
)

data class MovieWithStats(
    val movie: Movie,
    val rating: UserRating,
    val inWatchlist: Boolean
)
