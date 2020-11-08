package entities.movies

import entities.TmdbId
import entities.field.Actor
import entities.field.CommunityRating
import entities.field.Genre
import entities.field.Name
import entities.field.TmdbImageUrl
import entities.field.UserRating
import entities.field.Video

data class Movie(
    val id: TmdbId,
    val name: Name,
    val poster: TmdbImageUrl?,
    val backdrop: TmdbImageUrl?,
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val year: UInt,
    val rating: CommunityRating,
    val overview: String,
    val videos: Collection<Video>
)

data class MovieWithStats(
    val movie: Movie,
    val rating: UserRating,
    val inWatchlist: Boolean
)
