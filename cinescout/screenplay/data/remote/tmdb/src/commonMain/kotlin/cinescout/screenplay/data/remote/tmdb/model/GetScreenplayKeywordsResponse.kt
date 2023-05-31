package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayKeywordsResponse(
    @SerialName(Results)
    @Deprecated("Use keywords instead. This is needed as Tmdb uses different names for the same thing.")
    val results: List<Keyword>? = null,
    @SerialName(Keywords)
    val keywords: List<Keyword> = results ?: emptyList()
) {

    @Serializable
    data class Keyword(
        @SerialName(Id)
        val id: Int,
        @SerialName(Name)
        val name: String
    ) {

        companion object {

            const val Id = "id"
            const val Name = "name"
        }
    }

    companion object {

        const val Keywords = "keywords"
        const val Results = "results"
    }
}

data class GetScreenplayKeywordsResponseWithId(
    val response: GetScreenplayKeywordsResponse,
    val screenplayId: TmdbScreenplayId
)

infix fun GetScreenplayKeywordsResponse.withId(id: TmdbScreenplayId) =
    GetScreenplayKeywordsResponseWithId(this, id)
