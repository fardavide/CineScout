package cinescout.movies.domain.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TmdbMovieId(val value: Int)
