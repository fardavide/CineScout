package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody

typealias TraktScreenplaysRatingsExtendedResponse = List<TraktScreenplayRatingExtendedBody>

@Serializable
sealed interface TraktScreenplayRatingExtendedBody {

    val screenplay: TraktScreenplayExtendedBody

    val tmdbId: TmdbScreenplayId

    @SerialName(Rating)
    val rating: Int
}
