package cinescout.popular.data.remote.mapper

import cinescout.popular.data.remote.model.TraktScreenplaysPopularMetadataResponse
import cinescout.screenplay.domain.model.ScreenplayIds
import org.koin.core.annotation.Factory

@Factory
internal class TraktPopularMapper {

    fun toScreenplayIds(response: TraktScreenplaysPopularMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
