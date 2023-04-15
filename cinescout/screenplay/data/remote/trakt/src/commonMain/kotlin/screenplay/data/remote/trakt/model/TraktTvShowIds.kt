package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktTvShowIds(

    @SerialName(Tmdb)
    val tmdb: TmdbScreenplayId.TvShow,

    @SerialName(Trakt)
    val trakt: TraktScreenplayId.TvShow
) {

    companion object {

        const val Tmdb = "tmdb"
        const val Trakt = "trakt"
    }
}

/**
 * Same as [TraktTvShowIds] but with optional fields.
 */
@Serializable
data class OptTraktTvShowIds(

    @SerialName(TraktTvShowIds.Tmdb)
    val tmdb: TmdbScreenplayId.TvShow? = null,

    @SerialName(TraktTvShowIds.Trakt)
    val trakt: TraktScreenplayId.TvShow? = null
)
