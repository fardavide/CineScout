package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMultiRequest(

    @SerialName(Movies)
    val movies: List<TraktMovieIds>,

    @SerialName(TvShows)
    val tvShows: List<TraktTvShowIds>
) {

    companion object {

        const val Movies = "movies"
        const val TvShows = "shows"

        fun Single(screenplayId: TmdbScreenplayId) = when (screenplayId) {
            is TmdbScreenplayId.Movie -> TraktMultiRequest(
                movies = listOf(TraktMovieIds(tmdb = screenplayId)),
                tvShows = emptyList()
            )
            is TmdbScreenplayId.TvShow -> TraktMultiRequest(
                movies = emptyList(),
                tvShows = listOf(TraktTvShowIds(tmdb = screenplayId))
            )
        }
    }
}
