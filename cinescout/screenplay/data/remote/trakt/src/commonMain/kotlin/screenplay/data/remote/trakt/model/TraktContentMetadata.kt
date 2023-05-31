package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.ContentIds
import kotlinx.serialization.Serializable

typealias TraktContentMetadataResponse = List<TraktContentMetadataBody>

@Serializable
sealed interface TraktContentMetadataBody {

    fun ids(): ContentIds
}
