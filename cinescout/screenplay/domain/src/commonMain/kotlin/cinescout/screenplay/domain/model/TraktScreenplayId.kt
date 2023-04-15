package cinescout.screenplay.domain.model

import kotlinx.serialization.Serializable

sealed interface TraktScreenplayId {

    val value: Int

    @JvmInline
    @Serializable
    value class Movie(override val value: Int) : TraktScreenplayId

    @JvmInline
    @Serializable
    value class TvShow(override val value: Int) : TraktScreenplayId
}
