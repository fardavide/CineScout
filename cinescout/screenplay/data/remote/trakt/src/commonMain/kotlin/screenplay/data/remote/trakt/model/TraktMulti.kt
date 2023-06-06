package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TmdbEpisodeId
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbSeasonId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMultiRequest(

    @SerialName(Episodes)
    val episodes: List<OptTraktEpisodeMetadataBody>,

    @SerialName(Movies)
    val movies: List<OptTraktMovieMetadataBody>,

    @SerialName(Seasons)
    val seasons: List<OptTraktSeasonMetadataBody>,

    @SerialName(TvShows)
    val tvShows: List<OptTraktTvShowMetadataBody>
) {

    companion object {

        const val Episodes = "episodes"
        const val Movies = "movies"
        const val Seasons = "seasons"
        const val TvShows = "shows"

        fun Single(screenplayId: TmdbContentId) = when (screenplayId) {
            is TmdbEpisodeId -> TraktMultiRequest(
                episodes = listOf(OptTraktEpisodeMetadataBody(ids = OptTraktEpisodeIds(tmdb = screenplayId))),
                movies = emptyList(),
                seasons = emptyList(),
                tvShows = emptyList()
            )
            is TmdbMovieId -> TraktMultiRequest(
                episodes = emptyList(),
                movies = listOf(OptTraktMovieMetadataBody(ids = OptTraktMovieIds(tmdb = screenplayId))),
                seasons = emptyList(),
                tvShows = emptyList()
            )
            is TmdbSeasonId -> TraktMultiRequest(
                episodes = emptyList(),
                movies = emptyList(),
                seasons = listOf(OptTraktSeasonMetadataBody(ids = OptTraktSeasonIds(tmdb = screenplayId))),
                tvShows = emptyList()
            )
            is TmdbTvShowId -> TraktMultiRequest(
                episodes = emptyList(),
                movies = emptyList(),
                seasons = emptyList(),
                tvShows = listOf(OptTraktTvShowMetadataBody(ids = OptTraktTvShowIds(tmdb = screenplayId)))
            )
        }
    }
}
