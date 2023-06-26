package cinescout.recommended.data.remote.mapper

import cinescout.recommended.data.remote.model.TraktScreenplaysRecommendedMetadataResponse
import cinescout.screenplay.domain.model.id.ScreenplayIds
import org.koin.core.annotation.Factory

@Factory
internal class TraktRecommendedMapper {

    fun toScreenplayIds(response: TraktScreenplaysRecommendedMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
