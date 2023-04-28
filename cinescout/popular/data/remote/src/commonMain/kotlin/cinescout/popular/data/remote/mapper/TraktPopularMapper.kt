package cinescout.popular.data.remote.mapper

import cinescout.screenplay.domain.model.ScreenplayIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktScreenplayMetadataResponse

@Factory
internal class TraktPopularMapper {

    fun toScreenplayIds(response: TraktScreenplayMetadataResponse): List<ScreenplayIds> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.ids()
        }
}
