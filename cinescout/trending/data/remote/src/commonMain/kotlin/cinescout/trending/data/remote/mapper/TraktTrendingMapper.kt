package cinescout.trending.data.remote.mapper

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.trending.data.remote.model.TraktScreenplaysTrendingMetadataResponse
import org.koin.core.annotation.Factory

@Factory
internal class TraktTrendingMapper {

    fun toScreenplayIds(response: TraktScreenplaysTrendingMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
