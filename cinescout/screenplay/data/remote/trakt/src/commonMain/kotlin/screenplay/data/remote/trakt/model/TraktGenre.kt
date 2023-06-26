package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.GenreSlug
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktGenre(

    @SerialName(Name)
    val name: String,

    @SerialName(Slug)
    val slug: GenreSlug
) {

    companion object {

        const val Name = "name"
        const val Slug = "slug"
    }
}
