package cinescout.watchlist.data.remote.mapper

import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistExtendedResponse
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistMetadataResponse
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class TraktWatchlistMapper(
    private val screenplayMapper: TraktScreenplayMapper
) {

    fun toScreenplays(response: TraktScreenplaysWatchlistExtendedResponse): List<ScreenplayWithGenreSlugs> =
        response.map { ratingMetadataBody ->
            screenplayMapper.toScreenplayWithGenreSlugs(ratingMetadataBody.screenplay)
        }

    fun toScreenplayIds(response: TraktScreenplaysWatchlistMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
