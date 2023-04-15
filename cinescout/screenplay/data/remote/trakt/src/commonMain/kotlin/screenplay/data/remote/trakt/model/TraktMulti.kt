package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMultiRequest(

    @SerialName(Movies)
    val movies: List<OptTraktMovieMetadataBody>,

    @SerialName(TvShows)
    val tvShows: List<OptTraktTvShowMetadataBody>
) {

    companion object {

        const val Movies = "movies"
        const val TvShows = "shows"

        fun Single(screenplayId: TmdbScreenplayId) = when (screenplayId) {
            is TmdbScreenplayId.Movie -> TraktMultiRequest(
                movies = listOf(OptTraktMovieMetadataBody(ids = OptTraktMovieIds(tmdb = screenplayId))),
                tvShows = emptyList()
            )
            is TmdbScreenplayId.TvShow -> TraktMultiRequest(
                movies = emptyList(),
                tvShows = listOf(OptTraktTvShowMetadataBody(ids = OptTraktTvShowIds(tmdb = screenplayId)))
            )
        }
    }
}
