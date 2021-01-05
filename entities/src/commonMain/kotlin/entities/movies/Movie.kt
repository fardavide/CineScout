package entities.movies

import entities.TmdbId
import entities.model.Actor
import entities.model.CommunityRating
import entities.model.Genre
import entities.model.Name
import entities.model.TmdbImageUrl
import entities.model.UserRating
import entities.model.Video

data class Movie(
    val id: TmdbId,
    val name: Name,
    val poster: TmdbImageUrl?,
    val backdrop: TmdbImageUrl?,
    val actors: Collection<Actor>,
    val genres: Collection<Genre>,
    val year: UInt,
    val rating: CommunityRating,
    val popularity: Double,
    val overview: String,
    val videos: Collection<Video>
)

data class MovieWithStats(
    val movie: Movie,
    val rating: UserRating,
    val inWatchlist: Boolean
)
