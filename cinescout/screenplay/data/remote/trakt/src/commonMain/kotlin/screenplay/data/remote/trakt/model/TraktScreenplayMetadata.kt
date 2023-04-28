package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.serialization.Serializable

typealias TraktScreenplayMetadataResponse = List<TraktScreenplayMetadataBody>

@Serializable
sealed interface TraktScreenplayMetadataBody {

    fun ids(): ScreenplayIds
}
