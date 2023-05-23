package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TraktScreenplayIds {

    val tmdb: TmdbScreenplayId

    val trakt: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdb, trakt)

    companion object {

        const val Tmdb = "tmdb"
        const val Trakt = "trakt"
    }
}

/**
 * Same as [TraktScreenplayIds] but with optional fields.
 */
@Serializable
data class OptTraktScreenplayIds(

    @SerialName(TraktScreenplayIds.Tmdb)
    val tmdb: TmdbScreenplayId? = null,

    @SerialName(TraktScreenplayIds.Trakt)
    val trakt: TraktScreenplayId? = null
)
