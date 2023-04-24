package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.serialization.Serializable

@Serializable
sealed interface TraktScreenplayMetadataBody {

    fun ids(): ScreenplayIds
}
