package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktScreenplaysRatingsMetadataResponse = List<TraktScreenplayRatingMetadataBody>

@Serializable
sealed interface TraktScreenplayRatingMetadataBody {

    val tmdbId: TmdbScreenplayId

    @SerialName(Rating)
    val rating: Int
}
