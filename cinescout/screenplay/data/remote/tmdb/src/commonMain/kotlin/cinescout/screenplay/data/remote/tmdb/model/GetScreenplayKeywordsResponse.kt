package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayKeywordsResponse(

    @SerialName(Results)
    val keywords: List<Keyword>,

    @SerialName(TmdbScreenplay.Id)
    val screenplayId: TmdbScreenplayId
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

        const val Results = "results"
    }
}
