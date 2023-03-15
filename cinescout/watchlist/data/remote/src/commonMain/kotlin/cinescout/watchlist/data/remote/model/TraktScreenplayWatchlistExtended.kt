package cinescout.watchlist.data.remote.model

import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody

typealias TraktScreenplaysWatchlistExtendedResponse = List<TraktScreenplayWatchlistExtendedBody>

@Serializable
sealed interface TraktScreenplayWatchlistExtendedBody {

    val screenplay: TraktScreenplayExtendedBody
}
