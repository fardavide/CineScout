package cinescout.anticipated.data.remote.mapper

import cinescout.anticipated.data.remote.model.TraktScreenplaysAnticipatedMetadataResponse
import cinescout.screenplay.domain.model.ScreenplayIds
import org.koin.core.annotation.Factory

@Factory
internal class TraktAnticipatedMapper {

    fun toScreenplayIds(response: TraktScreenplaysAnticipatedMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids
        }
}
