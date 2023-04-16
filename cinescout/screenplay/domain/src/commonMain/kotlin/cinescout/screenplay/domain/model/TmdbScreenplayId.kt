package cinescout.screenplay.domain.model

import kotlinx.serialization.Serializable

sealed interface TmdbScreenplayId {

    val value: Int

    @JvmInline
    @Serializable
    value class Movie(override val value: Int) : TmdbScreenplayId

    @JvmInline
    @Serializable
    value class TvShow(override val value: Int) : TmdbScreenplayId
}
