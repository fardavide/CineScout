package cinescout.people.domain.model

import arrow.core.Option
import cinescout.media.domain.model.TmdbProfileImage

data class Person(
    val name: String,
    val profileImage: Option<TmdbProfileImage>,
    val tmdbId: TmdbPersonId
)
