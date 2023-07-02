package cinescout.history.data.remote.mapper

import cinescout.history.data.remote.model.TraktEpisodeHistoryMetadataBody
import cinescout.history.data.remote.model.TraktHistoryMetadataResponse
import cinescout.history.data.remote.model.TraktMovieHistoryMetadataBody
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.model.TvShowHistory
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayIdMapper
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Factory
internal class TraktHistoryMapper(
    private val idMapper: TraktScreenplayIdMapper
) {

    fun toHistories(response: TraktHistoryMetadataResponse): List<ScreenplayHistory> {
        val items = response.map { item ->
            when (item) {
                is TraktEpisodeHistoryMetadataBody -> item.show.ids to ScreenplayHistoryItem.Episode(
                    episodeNumber = item.episode.number,
                    id = item.id,
                    seasonNumber = item.episode.season,
                    watchedAt = item.watchedAt
                )

                is TraktMovieHistoryMetadataBody -> item.movie.ids to ScreenplayHistoryItem.Movie(
                    id = item.id,
                    watchedAt = item.watchedAt
                )
            }
        }.groupBy { (screenplayId, _) -> screenplayId }
        return items.map { (screenplayId, items) ->
            when (screenplayId) {
                is TraktMovieIds -> MovieHistory(
                    items = items.map { it.second as ScreenplayHistoryItem.Movie },
                    screenplayIds = idMapper.toScreenplayIds(screenplayId)
                )

                is TraktTvShowIds -> TvShowHistory(
                    items = items.map { it.second as ScreenplayHistoryItem.Episode },
                    screenplayIds = idMapper.toScreenplayIds(screenplayId)
                )
            }
        }
    }
}
