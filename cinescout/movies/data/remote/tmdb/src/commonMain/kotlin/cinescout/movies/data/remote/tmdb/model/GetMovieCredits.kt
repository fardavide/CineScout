package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.TmdbPersonId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetMovieCredits {

    @Serializable
    data class Response(

        @SerialName(Cast)
        val cast: List<CastMember>,

        @SerialName(Crew)
        val crew: List<CrewMember>,

        @SerialName(MovieId)
        val movieId: TmdbMovieId
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
            const val MovieId = "id"
            const val Name = "name"
            const val ProfilePath = "profile_path"
        }
    }
}
