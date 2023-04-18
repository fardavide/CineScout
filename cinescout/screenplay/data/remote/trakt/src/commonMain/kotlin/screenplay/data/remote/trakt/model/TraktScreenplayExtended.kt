package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.Serializable

typealias TraktScreenplaysExtendedResponse = List<TraktScreenplayExtendedBody>

@Serializable
sealed interface TraktScreenplayExtendedBody {

    val tmdbId: TmdbScreenplayId
    val traktId: TraktScreenplayId
}
