package cinescout.screenplay.domain.model.id

import kotlinx.serialization.Serializable

@Serializable
data class MovieIds(
    override val tmdb: TmdbMovieId,
    override val trakt: TraktMovieId
) : ScreenplayIds

@JvmInline
@Serializable
value class TmdbMovieId(override val value: Int) : TmdbScreenplayId

@JvmInline
@Serializable
value class TraktMovieId(override val value: Int) : TraktScreenplayId
