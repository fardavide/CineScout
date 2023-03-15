package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.Serializable

typealias TraktScreenplaysWatchlistMetadataResponse = List<TraktScreenplayWatchlistMetadataBody>

@Serializable
sealed interface TraktScreenplayWatchlistMetadataBody {

    val tmdbId: TmdbScreenplayId
}
