package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.ContentIds
import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktContentId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TraktContentIds {

    val tmdb: TmdbContentId

    val trakt: TraktContentId

    val ids: ContentIds
        get() = ContentIds(tmdb, trakt)

    companion object {

        const val Tmdb = "tmdb"
        const val Trakt = "trakt"
    }
}

/**
 * Same as [TraktContentIds] but with optional fields.
 */
@Serializable
data class OptTraktContentIds(
    @SerialName(TraktContentIds.Tmdb)
    val tmdb: TmdbScreenplayId? = null,
    @SerialName(TraktContentIds.Trakt)
    val trakt: TraktScreenplayId? = null
)
