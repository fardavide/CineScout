package cinescout.watchlist.data.remote.mapper

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistExtendedResponse
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistMetadataResponse
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class TraktWatchlistMapper(
    private val screenplayMapper: TraktScreenplayMapper
) {

    fun toScreenplays(response: TraktScreenplaysWatchlistExtendedResponse): List<Screenplay> =
        response.map { ratingMetadataBody ->
            screenplayMapper.toScreenplay(ratingMetadataBody.screenplay)
        }

    fun toScreenplayIds(response: TraktScreenplaysWatchlistMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
