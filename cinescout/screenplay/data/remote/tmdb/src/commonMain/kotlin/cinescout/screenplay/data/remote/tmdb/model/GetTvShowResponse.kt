package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.id.TmdbTvShowId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTvShowResponse(

    @Contextual
    @SerialName(TmdbTvShow.FirstAirDate)
    val firstAirDate: Date,

    @SerialName(TmdbScreenplay.Id)
    val id: TmdbTvShowId,

    @SerialName(TmdbTvShow.Name)
    val name: String,

    @SerialName(TmdbTvShow.Overview)
    val overview: String,

    @SerialName(TmdbTvShow.VoteAverage)
    val voteAverage: Double,

    @SerialName(TmdbTvShow.VoteCount)
    val voteCount: Int
)
