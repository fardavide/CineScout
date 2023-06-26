package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMovieResponse(

    @SerialName(TmdbScreenplay.Id)
    val id: TmdbMovieId,

    @SerialName(TmdbMovie.Overview)
    val overview: String,

    @SerialName(TmdbMovie.ReleaseDate)
    @Contextual
    val releaseDate: Date? = null,

    @SerialName(TmdbMovie.Title)
    val title: String,

    @SerialName(TmdbMovie.VoteAverage)
    val voteAverage: Double,

    @SerialName(TmdbMovie.VoteCount)
    val voteCount: Int
)
