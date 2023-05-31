package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovie(
    @SerialName(TmdbScreenplay.Id)
    val id: TmdbMovieId,
    @SerialName(Overview)
    val overview: String,
    @Contextual
    @SerialName(ReleaseDate)
    val releaseDate: Date? = null,
    @SerialName(Title)
    val title: String,
    @SerialName(VoteAverage)
    val voteAverage: Double,
    @SerialName(VoteCount)
    val voteCount: Int
) {

    companion object {

        const val Overview = "overview"
        const val ReleaseDate = "release_date"
        const val Title = "title"
        const val VoteAverage = "vote_average"
        const val VoteCount = "vote_count"
    }
}
