package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.Serializable
import kotlin.time.Duration

typealias TraktScreenplaysExtendedResponse = List<TraktScreenplayExtendedBody>

@Serializable
sealed interface TraktScreenplayExtendedBody {

    val ids: TraktScreenplayIds
    val overview: String
    val runtime: Duration?
    val tmdbId: TmdbScreenplayId
    val traktId: TraktScreenplayId
    val voteAverage: Double
    val voteCount: Int
}
