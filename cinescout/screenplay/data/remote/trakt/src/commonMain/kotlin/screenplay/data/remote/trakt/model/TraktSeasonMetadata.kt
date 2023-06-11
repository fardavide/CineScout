package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.ContentIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktSeasonsMetadataResponse = List<TraktSeasonMetadataBody>

@Serializable
@SerialName(TraktContentType.Season)
data class TraktSeasonMetadataBody(

    @SerialName(Number)
    val number: SeasonNumber,

    @SerialName(TraktContent.Ids)
    val ids: TraktSeasonIds

) : TraktContentMetadataBody {

    override fun ids(): ContentIds = SeasonIds(
        trakt = ids.trakt,
        tmdb = ids.tmdb
    )

    companion object {

        const val Number = "number"
    }
}

/**
 * Same as [TraktSeasonMetadataBody] but with optional fields.
 */
@Serializable
data class OptTraktSeasonMetadataBody(
    @SerialName(TraktContent.Ids)
    val ids: OptTraktSeasonIds
)
