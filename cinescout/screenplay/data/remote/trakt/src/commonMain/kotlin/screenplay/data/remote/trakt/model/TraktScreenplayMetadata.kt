package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.Serializable

typealias TraktScreenplaysMetadataResponse = List<TraktScreenplayMetadataBody>

@Serializable
sealed interface TraktScreenplayMetadataBody {

    val tmdbId: TmdbScreenplayId
}
