package cinescout.movies.domain.model

import com.soywiz.klock.Date

data class Movie(
    val releaseDate: Date,
    val title: String,
    val tmdbId: TmdbMovieId
)
