package cinescout.movies.domain.model

import arrow.core.Option

data class Person(
    val name: String,
    val profileImage: Option<TmdbProfileImage>,
    val tmdbId: TmdbPersonId
)
