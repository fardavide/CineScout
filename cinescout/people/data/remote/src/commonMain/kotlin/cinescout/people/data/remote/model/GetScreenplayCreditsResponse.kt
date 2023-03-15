package cinescout.people.data.remote.model

import cinescout.people.domain.model.TmdbPersonId
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayCreditsResponse(

    @SerialName(Cast)
    val cast: List<CastMember>,

    @SerialName(Crew)
    val crew: List<CrewMember>,

    @SerialName(Id)
    val screenplayId: TmdbScreenplayId
) {

    @Serializable
    data class CastMember(

        @SerialName(Character)
        val character: String?,

        @SerialName(Id)
        val id: TmdbPersonId,

        @SerialName(Name)
        val name: String,

        @SerialName(ProfilePath)
        val profilePath: String?
    ) {

        companion object {

            const val Character = "character"
        }
    }

    @Serializable
    data class CrewMember(

        @SerialName(Id)
        val id: TmdbPersonId,

        @SerialName(Job)
        val job: String?,

        @SerialName(Name)
        val name: String,

        @SerialName(ProfilePath)
        val profilePath: String?
    ) {

        companion object {

            const val Job = "job"
        }
    }

    companion object {

        const val Cast = "cast"
        const val Crew = "crew"
        const val Id = "id"
        const val TvShowId = "id"
        const val Name = "name"
        const val ProfilePath = "profile_path"
    }
}