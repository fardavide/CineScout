package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMultiRequest(

    @SerialName(Movies)
    val movies: List<TraktMovieMetadataBody>,

    @SerialName(TvShows)
    val tvShows: List<TraktTvShowMetadataBody>
) {

    companion object {

        const val Movies = "movies"
        const val TvShows = "shows"

        fun Single(screenplayId: TmdbScreenplayId) = when (screenplayId) {
            is TmdbScreenplayId.Movie -> TraktMultiRequest(
                movies = listOf(TraktMovieMetadataBody(ids = TraktMovieIds(tmdb = screenplayId))),
                tvShows = emptyList()
            )
            is TmdbScreenplayId.TvShow -> TraktMultiRequest(
                movies = emptyList(),
                tvShows = listOf(TraktTvShowMetadataBody(ids = TraktTvShowIds(tmdb = screenplayId)))
            )
        }
    }
}
